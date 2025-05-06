package system;

import exceptions.RootException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private InetSocketAddress address;
    private File file;
    public static String data_path = null;
    private Selector selector;
    private ServerSocketChannel serverChannel;

    public void initialize(int port, String filepath) throws IOException, RootException {
        this.address = new InetSocketAddress(port);
        Logger.getLogger(Server.class.getName()).info("Server was started at address: " + address);
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.bind(address);
        this.serverChannel.configureBlocking(false);
        this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.file = new File(filepath);

        if (file.canRead() && file.canWrite()) {
            new CommandManager();
            try {
                Logger.getLogger(Server.class.getName()).info("Downloading data from file...");
                CollectionManager.getInstance().setLabWorkCollection(DumpManager.getInstance(filepath).readLabWorks());
                data_path = filepath;
                Logger.getLogger(Server.class.getName()).info("Data was downloaded");
            } catch (Exception e) {
                Logger.getLogger(Server.class.getName()).warning("Error while reading file\n" + filepath);
                System.out.println(e.getMessage());
                System.exit(0);
            }
        } else {
            Logger.getLogger(Server.class.getName()).warning("File is not readable");
            throw new RootException();
        }
        Logger.getLogger(Server.class.getName()).info("Server is initialized");
    }

    public void start() {
        Logger.getLogger(Server.class.getName()).info("Server started and listening for connections");

        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        acceptClient(key);
                    } else if (key.isReadable()) {
                        handleClientRequest(key);
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).severe("Server error: " + e.getMessage());
        }
    }

    private void acceptClient(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        Logger.getLogger(Server.class.getName()).info("New client connected: " + client.getRemoteAddress());
    }

    private void handleClientRequest(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);

        try {
            while (lengthBuffer.hasRemaining()) {
                if (client.read(lengthBuffer) == -1) {
                    throw new ClosedChannelException();
                }
            }
            lengthBuffer.flip();
            int messageLength = lengthBuffer.getInt();

            if (messageLength <= 0 || messageLength > 10_000_000) {
                throw new IOException("Invalid message length: " + messageLength);
            }

            ByteBuffer dataBuffer = ByteBuffer.allocate(messageLength);
            while (dataBuffer.hasRemaining()) {
                int read = client.read(dataBuffer);
                if (read == -1) {
                    throw new ClosedChannelException();
                }
            }
            dataBuffer.flip();

            Request request;
            try (ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(dataBuffer.array(), 0, messageLength))) {
                request = (Request) ois.readObject();
            }

            Request response = processCommand(request);

            sendResponse(client, response);

        } catch (Exception e) {
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Request handling failed", e);
            if (client.isOpen()) {
                try {

                    sendResponse(client, new Request("Error: " + e.getMessage(), null, null));
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.FINE, "Failed to send error", ex);
                }
            }
            key.cancel();
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.FINE, "Error closing client", ex);
            }
        }
    }


    private Request processCommand(Request request) {
        try {
            String resultMessage;
            if ("save".equals(request.getMessage())) {
                DumpManager.getInstance(data_path).writeLabWorks(
                        CollectionManager.getInstance().getLabWorkCollection());
                resultMessage = "Collection saved";
            } else {
                resultMessage = CommandManager.startExecutingClientMode(request);
            }

            return new Request(resultMessage, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Request("Error: " + e.getMessage(), null, null);
        }
    }


    private void sendResponse(SocketChannel client, Request response) throws IOException {
        if (!client.isOpen()) {
            throw new ClosedChannelException();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
            byte[] responseData = baos.toByteArray();


            ByteBuffer buffer = ByteBuffer.allocate(4 + responseData.length)
                    .order(ByteOrder.BIG_ENDIAN);
            buffer.putInt(responseData.length);
            buffer.put(responseData);
            buffer.flip();

            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
        }
    }


}
