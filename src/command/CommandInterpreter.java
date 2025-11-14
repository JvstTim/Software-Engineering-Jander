package command;

import java.util.*;

public class CommandInterpreter {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command c) {
        commands.put(c.name().toLowerCase(Locale.ROOT), c);
    }

    public boolean tryExecute(String raw) {
        if (raw == null || !raw.startsWith("/")) return false;
        String[] parts = raw.trim().split("\\s+");
        if (parts.length == 0) return false;

        String cmdName = parts[0].substring(1).toLowerCase(Locale.ROOT);
        Command cmd = commands.get(cmdName);
        if (cmd == null) return false;

        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        cmd.execute(args);
        return true;
    }

    public String helpText() {
        StringBuilder sb = new StringBuilder("Befehle:\n");
        commands.values().stream()
                .sorted(Comparator.comparing(Command::name))
                .forEach(c -> sb.append("  ").append(c.help()).append("\n"));
        return sb.toString();
    }
}
