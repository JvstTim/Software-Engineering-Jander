import cli.ConsoleUI;
import communication.TcpNetwork;
import controller.BasicChatController;
import controller.ChatController;
import network.Network;
import ui.ChatUI;
import user.SimpleUser;
import user.User;

public class Main {
    public static void main(String[] args) {
        Network net = new TcpNetwork();
        ChatUI ui = new ConsoleUI();
        User user = new SimpleUser();

        ChatController controller = new BasicChatController(net, ui, user);
        controller.startChat();
    }
}
