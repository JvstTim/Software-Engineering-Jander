package command;

import controller.ChatController;

public class DisconnectCommand implements Command {
    private final ChatController controller;
    public DisconnectCommand(ChatController controller) { this.controller = controller; }
    @Override public String name() { return "disconnect"; }
    @Override public String help() { return "/disconnect"; }

    @Override
    public void execute(String[] args) {
        controller.sendMessage("/__internal_disconnect");
    }
}
