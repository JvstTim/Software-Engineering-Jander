package cli;

import ui.console.Console;

import java.util.Scanner;

public class ConsoleUI implements Console {
    private final Scanner scanner = new Scanner(System.in);
    private volatile boolean running = false;
    private volatile String status = "";

    @Override public void clearScreen() { System.out.print("\033[H\033[2J"); System.out.flush(); }

    @Override public String formatText(String text, String style) { return text; }

    @Override public boolean confirmAction(String prompt) {
        System.out.print(prompt + " [y/N]: ");
        String s = scanner.nextLine().trim().toLowerCase();
        return s.equals("y") || s.equals("yes") || s.equals("j") || s.equals("ja");
    }

    @Override public void displayMessage(String sender, String message) {
        System.out.println((sender == null ? "" : sender + ": ") + message);
    }

    @Override public void setStatus(String status) { this.status = status; }

    @Override public String getStatus() { return status; }

    @Override public void showStatus(String info) { System.out.println("[Status] " + info); }

    @Override public String getUserInput() { return scanner.nextLine(); }

    @Override public void start() { running = true; }

    @Override public void stop() { running = false; }

    public boolean isRunning() { return running; }
}
