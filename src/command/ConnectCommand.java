package command;

import controller.ChatController;

public class ConnectCommand implements Command {
    private final ChatController controller;

    public ConnectCommand(ChatController controller) {
        this.controller = controller;
    }

    @Override public String name() { return "connect"; }
    @Override public String help() { return "/connect <host> <port>"; }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException(help());
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        controller.receiveMessage("System: Connecting to " + host + ":" + port + " ...");
        controller.sendMessage("/__internal_connect " + host + " " + port);
    }
}
