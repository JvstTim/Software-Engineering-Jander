package controller;

public interface ChatController {
    void startChat();
    void stopChat();
    void sendMessage(String message);
    void receiveMessage(String message);
}
