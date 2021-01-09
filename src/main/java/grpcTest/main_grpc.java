package grpcTest;

public class main_grpc {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        String city = args[1];
        String zkHost = args[2];
        // function should be in [Ride, User]
        String function = args[3];
        city_server cityServer = new city_server(port, city, zkHost, function);
        cityServer.start();
        System.out.println("Server started");
        cityServer.blockUntilShutdown();
    }
}
