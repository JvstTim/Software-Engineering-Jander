package command;

import controller.ChatController;

public class ListenCommand implements Command {
    private final ChatController controller;

    public ListenCommand(ChatController controller) { this.controller = controller; }
    @Override public String name() { return "listen"; }
    @Override public String help() { return "/listen <port>"; }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException(help());
        int port = Integer.parseInt(args[0]);
        controller.receiveMessage("System: Listening on " + port + " ...");
        controller.sendMessage("/__internal_listen " + port);
    }
}
