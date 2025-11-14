package ui;

public interface ChatUI {
    void start();
    void stop();
    void displayMessage(String sender, String message);
    void showStatus(String info);

    default String getUserInput() { return null; }
}
