package ui.console;

import ui.ChatUI;

public interface Console extends ChatUI {
    void clearScreen();
    String formatText(String text, String style);
    boolean confirmAction(String prompt);
    String getUserInput();
    void setStatus(String status);
    String getStatus();
}
