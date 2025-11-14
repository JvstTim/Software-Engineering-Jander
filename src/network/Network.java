package network;

public interface Network extends AutoCloseable {
    void connect(String host, int port);
    void startServer(int port);
    void sendMessage(String message);
    String receiveMessage();    // null zurückgeben, wenn gerade nichts da ist
    boolean isConnected();
    @Override void close();
}
