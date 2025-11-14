package command;

import controller.ChatController;

public class QuitCommand implements Command {
    private final ChatController controller;
    public QuitCommand(ChatController controller) { this.controller = controller; }
    @Override public String name() { return "quit"; }
    @Override public String help() { return "/quit"; }

    @Override
    public void execute(String[] args) {
        controller.sendMessage("/__internal_quit");
    }
}
