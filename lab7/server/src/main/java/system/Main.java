package system;

import managers.CommandManager;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar server.jar <port>");
            Logger.getLogger(Main.class.getName()).warning("Port not specified");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            Server server = new Server();
            server.initialize(port);
            server.start(); // Сервер сам обрабатывает shutdown через свой метод
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).severe("Server failed: " + e.getMessage());
            System.exit(1);
        }
    }
}