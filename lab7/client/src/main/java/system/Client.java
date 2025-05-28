package system;

import exceptions.NoArgumentException;
import exceptions.WrongArgumentException;
import managers.ExecuteScriptCommand;
import managers.LabWorkAsker;
import managers.User;
import models.LabWork;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    public void initialize(String host, int port) throws IOException {
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to server.");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();

            try {
                if (command.equals("exit")) {
                    System.exit(0);
                }

                if (!User.isAuth()) {
                    if (command.equals("register") || command.equals("login")) {
                        authCommand(command, scanner);
                    } else {
                        System.out.println("Please login or register first.");
                    }
                } else {
                    if (command.equals("register") || command.equals("login")) {
                        System.out.println("You are already logged in.");
                    } else {
                        handleCommand(command, scanner);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Connection error: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void authCommand(String command, Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        Request request = new Request(command, null, null, username, password.toCharArray());
        sendRequest(request);

        Request response = receiveResponse();
        System.out.println("Server: " + response.getMessage());

        if (response.getMessage().toLowerCase().contains("success")) {
            User.getInstance(username, password.toCharArray()).setAuth(true);
        }
    }

    private void handleCommand(String command, Scanner scanner) throws Exception {
        LabWork labWork = null;
        String key = null;
        boolean isClientCommand = false;

        if (command.startsWith("remove_by_id") || command.startsWith("remove_by_author") || command.startsWith("count_less_than_author")) {
            key = command.split(" ")[1];
        } else if (command.equals("add")) {
            labWork = LabWorkAsker.createLabWork();
        }else if (command.split(" ")[0].equals("execute_script")) {
            ExecuteScriptCommand.execute(command);
            isClientCommand = true;
        }else if (command.startsWith("updateId")) {
            key = command.split(" ")[1];
            labWork = LabWorkAsker.createLabWork();
            labWork.setId(Long.parseLong(key));
        }
        if (!isClientCommand) {
            Request request = new Request(command, labWork, key, User.getInstance().getName(), User.getInstance().getPasswd());
            sendRequest(request);}

        Request response = receiveResponse();
        System.out.println("Server: " + response.getMessage());
    }

    public static void sendRequest(Request request) throws IOException {
        output.writeObject(request);
        output.flush();
    }

    private Request receiveResponse() throws IOException, ClassNotFoundException {
        return (Request) input.readObject();
    }
}
