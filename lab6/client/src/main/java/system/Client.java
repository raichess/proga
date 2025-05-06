package system;

import models.LabWork;
import managers.LabWorkAsker;
import managers.ExecuteScriptCommand;
import exceptions.NoArgumentException;
import exceptions.WrongArgumentException;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public void initialize(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        System.out.println("Connected to server. Enter commands...");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                System.exit(0);
            }
            if (!command.isEmpty()) {
                try {
                    LabWork labWork = new LabWork();
                    String key = null;
                    boolean isExecuteScriptCommand = false;

                    if (command.contains("remove_by_id") || command.contains("remove_by_author") || command.contains("count_less_than_author")) {
                        if (command.split(" ").length == 2) {
                            key = command.split(" ")[1];
                        }
                    } else if (command.equals("add")) {
                        labWork = LabWorkAsker.createLabWork();
                    } else if (command.startsWith("execute_script")) {
                        ExecuteScriptCommand.execute(command);
                        isExecuteScriptCommand = true;
                    } else if (command.startsWith("updateId")) {
                        if (command.split(" ").length == 2) {
                            key = command.split(" ")[1];
                        }
                        labWork = LabWorkAsker.createLabWork();
                    }

                    if (!isExecuteScriptCommand) {
                        Request request = new Request(command, labWork, key);
                        Request response = sendRequest(request);
                        System.out.println(response.getMessage());
                    }
                } catch (WrongArgumentException | NoArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
        }
    }

    public static Request sendRequest(Request request) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteStream)) {
            oos.writeObject(request);
            oos.flush();
        }
        byte[] requestData = byteStream.toByteArray();

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        lengthBuffer.putInt(requestData.length);
        outputStream.write(lengthBuffer.array());
        outputStream.write(requestData);
        outputStream.flush();

        return getResponse();
    }

    private static Request getResponse() throws IOException, ClassNotFoundException {
        byte[] lengthBytes = new byte[4];
        int bytesRead = 0;
        while (bytesRead < 4) {
            int read = inputStream.read(lengthBytes, bytesRead, 4 - bytesRead);
            if (read == -1) throw new EOFException("Connection closed prematurely while reading length");
            bytesRead += read;
        }

        int length = ByteBuffer.wrap(lengthBytes).order(ByteOrder.BIG_ENDIAN).getInt();
        if (length <= 0 || length > 10_000_000) {
            throw new IOException("Invalid message length: " + length);
        }

        byte[] responseData = new byte[length];
        bytesRead = 0;
        while (bytesRead < length) {
            int read = inputStream.read(responseData, bytesRead, length - bytesRead);
            if (read == -1) throw new EOFException("Connection closed prematurely while reading data");
            bytesRead += read;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(responseData))) {
            return (Request) ois.readObject();
        }
    }
}
