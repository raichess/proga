package system;



public class Main {
    public static void main(String[] args){
        try {
            if (args.length != 2) {
                System.out.println("Wrong argument in address or host");
                return;
            }
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Client client = new Client();
            client.initialize(host, port);
            client.start();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}