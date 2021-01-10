package grpcServers;

public class main_grpc {
    private static final String RIDE = "Ride";
    private static final String USER = "User";
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        String city = args[1];
        String zkHost = args[2];
        // function should be in [Ride, User]
        String function = args[3];
        if (function.equals(RIDE)){
            CityServerRide cityServer = new CityServerRide(port, city, zkHost);
            cityServer.start();
            System.out.println("Server started");
            cityServer.blockUntilShutdown();
        }else if (function.equals(USER)) {
            CityServerUser cityServer = new CityServerUser(port, city, zkHost);
            cityServer.start();
            System.out.println("Server started");
            cityServer.blockUntilShutdown();
        }else{
            throw new RuntimeException("Invallid function argument");
        }

    }
}
