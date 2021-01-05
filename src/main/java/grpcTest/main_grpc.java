package grpcTest;

public class main_grpc {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        city_server a = new city_server(port);
        a.start();
        System.out.println("Server started");
        a.blockUntilShutdown();
    }
}
