package message;

public interface MessageHandler {
    void handleIncomingMessage(String message);
    void handleOutgoingMessage(String message);
    void handleSystemMessage(String info);
}
