package command;

import controller.ChatController;

public class SetNameCommand implements Command {
    private final ChatController controller;

    public SetNameCommand(ChatController controller) { this.controller = controller; }

    @Override public String name() { return "setname"; }
    @Override public String help() { return "/setName <username>"; }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException(help());
        String name = String.join(" ", args);
        controller.sendMessage("/__internal_setname " + name);
    }
}
