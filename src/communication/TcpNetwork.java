package communication;

import network.Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpNetwork implements Network {
    private volatile Socket socket;
    private volatile ServerSocket serverSocket;
    private volatile BufferedReader in;
    private volatile PrintWriter out;
    private volatile boolean connected = false;

    @Override
    public synchronized void connect(String host, int port) {
        try {
            close();
            this.socket = new Socket(host, port);
            attachStreams(this.socket);
            this.connected = true;
        } catch (IOException e) {
            throw new RuntimeException("Connect failed: " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void startServer(int port) {
        try {
            close();
            this.serverSocket = new ServerSocket(port);
            // accept in Hintergrund-Thread
            Thread t = new Thread(() -> {
                try {
                    Socket s = serverSocket.accept();
                    synchronized (TcpNetwork.this) {
                        socket = s;
                        attachStreams(s);
                        connected = true;
                    }
                } catch (IOException e) {
                    // Server wurde geschlossen -> ignorieren
                }
            }, "TcpNetwork-Accept");
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            throw new RuntimeException("Listen failed: " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void sendMessage(String message) {
        if (!connected || out == null) throw new IllegalStateException("Not connected");
        out.println(message);
        out.flush();
    }

    @Override
    public String receiveMessage() {
        try {
            BufferedReader localIn;
            synchronized (this) { localIn = in; }
            if (!connected || localIn == null) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                return null;
            }
            if (!localIn.ready()) {
                try { Thread.sleep(10); } catch (InterruptedException ignored) {}
                return null;
            }
            String line = localIn.readLine();
            if (line == null) {
                // Verbindung wurde geschlossen
                close();
                return null;
            }
            return line;
        } catch (IOException e) {
            close();
            return null;
        }
    }

    @Override
    public synchronized void close() {
        connected = false;
        try { if (in != null) in.close(); } catch (IOException ignored) {}
        if (out != null) out.close();
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        in = null; out = null; socket = null; serverSocket = null;
    }

    @Override
    public synchronized boolean isConnected() {
        return connected;
    }

    private void attachStreams(Socket s) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
    }
}
