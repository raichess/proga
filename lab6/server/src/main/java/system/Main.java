package system;

import managers.CommandManager;

import java.io.File;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                CommandManager.startExecutingServerMode(new Request("save", null, null));
                Logger.getLogger(Main.class.getName()).info("Collection saved on shutdown");
            } catch (Exception e) {
                Logger.getLogger(Main.class.getName()).warning("Failed to save on shutdown: " + e.getMessage());
            }
        }));

        if (args.length != 2) {
            System.out.println("Usage: java -jar server.jar <port> <file_path>");
            Logger.getLogger(Main.class.getName()).warning("Something wrong with settings of server or file");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String host = args[1];

        if (!new File(host).exists()) {
            System.err.println("Error: File not found");
            System.exit(1);
        }
        try {
            Server server = new Server();
            server.initialize(port, host);
            server.start();
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).warning("Something wrong with settings of server or file" + e.getMessage());
            System.exit(1);
        }
    }
}