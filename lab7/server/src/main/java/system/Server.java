package system;

import clientLog.ClientHandler;
import exceptions.RootException;
import managers.CollectionManager;
import managers.CommandManager;
import DBlogic.PostgreSQLManager;
import models.LabWork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Server {
    private ServerSocket serverSocket;
    private final ForkJoinPool readPool = ForkJoinPool.commonPool(); // Для чтения запросов
    private final ExecutorService processingPool = Executors.newFixedThreadPool(4); // Для обработки запросов
    private volatile boolean isRunning = true;

    public void initialize(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        Logger.getLogger(Server.class.getName()).info("Server started on port: " + port);

        PostgreSQLManager.createBase();

        new CommandManager();
        try {
            CollectionManager.getInstance().loadCollectionFromDB();
            Logger.getLogger(Server.class.getName()).info("Data loaded from DB");
        } catch (Exception e) {
            Logger.getLogger(Server.class.getName()).warning("Error loading data: " + e.getMessage());
            System.exit(1);
        }
    }

    public void start() {
        Logger.getLogger(Server.class.getName()).info("Server is listening...");
        startServerCommandRead();

        try {
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                Logger.getLogger(Server.class.getName()).info("Client connected: " + clientSocket.getRemoteSocketAddress());

                // Используем ForkJoinPool для чтения запросов
                readPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (isRunning) {
                Logger.getLogger(Server.class.getName()).severe("Failed to accept connection: " + e.getMessage());
            }
        } finally {
            shutdown();
        }
    }

    private void handleClient(Socket socket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (!socket.isClosed() && isRunning) {
                Request request = (Request) in.readObject();
                ClientHandler.authUserCommand(request.getName(), request.getPasswd());

                processingPool.execute(() -> {
                    String responseMsg = CommandManager.startExecutingClientMode(request);
                    Request response = new Request(responseMsg, new LabWork(), null,
                            request.getName(), request.getPasswd());

                    try {
                        out.writeObject(response);
                        out.flush();
                    } catch (IOException e) {
                        Logger.getLogger(Server.class.getName())
                                .warning("Failed to send response: " + e.getMessage());
                        closeSocket(socket);
                    }
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(Server.class.getName())
                    .info("Client disconnected or error: " + e.getMessage());
        } finally {
            closeSocket(socket);
        }
    }

    private void closeSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).fine("Error closing socket: " + e.getMessage());
        }
    }

    private void startServerCommandRead() {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String input;
                while ((input = reader.readLine()) != null && isRunning) {
                    if (input.equals("exit") || input.equals("save")) {
                        CommandManager.startExecutingServerMode(new Request(input, null, null, null, null));
                        if (input.equals("exit")) {
                            shutdown();
                        }
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(Server.class.getName()).severe("Console input error: " + e.getMessage());
            }
        }).start();
    }

    private void shutdown() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).warning("Error closing server socket: " + e.getMessage());
        }

        readPool.shutdown();
        processingPool.shutdown();
        Logger.getLogger(Server.class.getName()).info("Server shutdown completed");
    }
}