package command;

public interface Command {
    String name();
    String help();
    void execute(String[] args);
}
