package controller;

import cli.ConsoleUI;
import command.*;
import communication.TcpNetwork;
import message.MessageHandler;
import network.Network;
import ui.ChatUI;
import user.User;

public class BasicChatController implements ChatController, MessageHandler {
    private final Network network;
    private final ChatUI ui;
    private final User user;
    private final CommandInterpreter interpreter;

    private volatile boolean running = false;

    public BasicChatController(Network network, ChatUI ui, User user) {
        this.network = network;
        this.ui = ui;
        this.user = user;
        this.interpreter = new CommandInterpreter();
        registerDefaultCommands();
    }

    private void registerDefaultCommands() {
        interpreter.register(new ConnectCommand(this));
        interpreter.register(new ListenCommand(this));
        interpreter.register(new DisconnectCommand(this));
        interpreter.register(new SetNameCommand(this));
        interpreter.register(new QuitCommand(this));
    }

    @Override
    public void startChat() {
        running = true;
        ui.start();
        ui.showStatus("Willkommen! " + interpreter.helpText());

        Thread rx = new Thread(() -> {
            while (running) {
                String msg = network.receiveMessage();
                if (msg != null) {
                    handleIncomingMessage(msg);
                }
                try { Thread.sleep(10); } catch (InterruptedException ignored) {}
            }
        }, "ReceiveLoop");
        rx.setDaemon(true);
        rx.start();

        while (running && (ui instanceof ConsoleUI) && ((ConsoleUI) ui).isRunning()) {
            String line = ui.getUserInput();
            if (line == null) continue;

            if (interpreter.tryExecute(line)) {
                continue;
            }

            if (network.isConnected()) {
                handleOutgoingMessage(line);
                network.sendMessage(user.getUsername() + ": " + line);
            } else {
                ui.showStatus("Nicht verbunden. Nutze z.B. /connect <host> <port> oder /listen <port>");
            }
        }
    }

    @Override
    public void stopChat() {
        running = false;
        network.close();
        ui.stop();
    }

    @Override
    public void sendMessage(String message) {
        if (message.startsWith("/__internal_connect ")) {
            String[] p = message.split("\\s+");
            String host = p[1];
            int port = Integer.parseInt(p[2]);
            network.connect(host, port);
            ui.showStatus("Verbunden mit " + host + ":" + port);
            return;
        }
        if (message.startsWith("/__internal_listen ")) {
            String[] p = message.split("\\s+");
            int port = Integer.parseInt(p[1]);
            network.startServer(port);
            ui.showStatus("Warte auf Verbindung auf Port " + port + " ...");
            return;
        }
        if (message.equals("/__internal_disconnect")) {
            network.close();
            ui.showStatus("Verbindung getrennt.");
            return;
        }
        if (message.startsWith("/__internal_setname ")) {
            String name = message.substring("/__internal_setname ".length()).trim();
            handleSystemMessage("Name gesetzt auf: " + name);
            return;
        }
        if (message.equals("/__internal_quit")) {
            stopChat();
            return;
        }

        if (network.isConnected()) {
            network.sendMessage(message);
        }
    }

    @Override
    public void receiveMessage(String message) {
        handleIncomingMessage(message);
    }

    @Override
    public void handleIncomingMessage(String message) {
        ui.displayMessage(null, message);
    }

    @Override
    public void handleOutgoingMessage(String message) {
        ui.displayMessage(user.getUsername(), message);
    }

    @Override
    public void handleSystemMessage(String info) {
        ui.showStatus(info);
        if (info.startsWith("Name gesetzt auf: ")) {
            String name = info.substring("Name gesetzt auf: ".length());
            if (user != null) user.setUsername(name);
        }
    }
}
