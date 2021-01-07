package grpcTest;

import TaxiRide.City;
import com.google.protobuf.RepeatedFieldBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import TaxiRide.Ride;
import TaxiRide.User;
import RestService.repository.*;
import zookeeper.Member;
import zookeeper.ZkServiceImpl;
import java.net.InetAddress;


public class city_server {
    private final int port;
    private RideRepository rideRepository;
    private final Server server;
    private final String city;
    public static ZkServiceImpl zkService;

    public city_server(int port, String city, String host) throws IOException {
        this(ServerBuilder.forPort(port), port, city, host);
    }
    public city_server(ServerBuilder<?> serverBuilder, int port, String city, String host) {
        this.port = port;
        server = serverBuilder.addService(new TaxiImpl())
                .build();
        this.city = city;
        this.rideRepository = new RideRepository();

        zkService = new ZkServiceImpl(host);
        System.out.println("connected to host: " + host);

        zkService.createParentNode(null);
        System.out.println("(if needed) create the zk main folder");

        zkService.createParentNode(city);
        System.out.println("(if needed) create the zk city folder");

        try {
            String ip_host = InetAddress.getLocalHost().getHostAddress() + ":" + port;
            zkService.addToLiveNodes(ip_host, ip_host, city);
            System.out.println("Znode added to zk sever");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException("Cant connect to local ip");

        }
        zkService.registerChildrenChangeWatcher(zkService.MEMBER + "/" + city, new Member());
        System.out.println("added watcher / listener");
    }

    class TaxiImpl extends TaxiServiceGrpc.TaxiServiceImplBase{
        @Override
        public void ride(TaxiRideProto.RideRequest request, StreamObserver<TaxiRideProto.RideRequest> responseObserver) {
            TaxiRideProto.City source = request.getStartLocation();
            TaxiRideProto.City dst = request.getEndLocation();
            TaxiRideProto.Date date = request.getDate();
            LocalDate local_date = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            Ride ride = new Ride(request.getFirstName(),
                    request.getLastName(),
                    request.getPhoneNumber(),
                    new City(source.getCityName(), source.getCityId(), source.getX(), source.getY()),
                    new City(dst.getCityName(), dst.getCityId(), dst.getX(), dst.getY()),
                    local_date,
                    request.getVacancies(),
                    request.getPd());
            System.out.println(ride);
            rideRepository.save(ride);
            System.out.println(rideRepository.findAll());
        }

        @Override
        public void user(TaxiRideProto.UserRequest request, StreamObserver<TaxiRideProto.UserRequest> responseObserver) {
            TaxiRideProto.City source = request.getLocation();
            TaxiRideProto.Date date = request.getDate();
            LocalDate local_date = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            ArrayList<City> path = new ArrayList<>();
            for (TaxiRideProto.City city : request.getCityPathList()){
                path.add(new City(city.getCityName(), city.getCityId(), city.getX(), city.getY()));
            }
            User user = new User(new City(source.getCityName(), source.getCityId(), source.getX(), source.getY()),
                    request.getFirstName(),
                    request.getLastName(),
                    local_date,
                    path);
                    
            System.out.println(user);
        }
    }


        public void start() throws IOException {
        try {
            server.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    city_server.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
