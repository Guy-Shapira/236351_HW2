package grpcTest;

import TaxiRide.City;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import TaxiRide.Ride;
import TaxiRide.User;
import RestService.repository.*;

public class city_server {
    private final int port;
    private RideRepository rides;
    private UserRepository users;
    private final Server server;

    public city_server(int port) throws IOException {
        this(ServerBuilder.forPort(port), port);
    }
    public city_server(ServerBuilder<?> serverBuilder, int port) {
        this.port = port;
        server = serverBuilder.addService(new TaxiImpl())
                .build();
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
        }

        @Override
        public void user(TaxiRideProto.UserRequest request, StreamObserver<TaxiRideProto.UserRequest> responseObserver) {
            TaxiRideProto.City source = request.getLocation();
            User user = new User(new City(source.getCityName(), source.getCityId(), source.getX(), source.getY()),
                    request.getFirstName(),
                    request.getLastName());
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
