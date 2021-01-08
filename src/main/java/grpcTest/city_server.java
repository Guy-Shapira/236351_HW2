package grpcTest;

import TaxiRide.City;
import Utils.protoUtils;
import com.google.protobuf.RepeatedFieldBuilder;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import TaxiRide.Ride;
import TaxiRide.User;
import RestService.repository.*;
import zookeeper.Member;
import zookeeper.ZkServiceImpl;
import java.net.InetAddress;

import static RestService.main.zkService;


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

    class TaxiImpl extends TaxiServiceGrpc.TaxiServiceImplBase {
        @Override
        public void ride(TaxiRideProto.RideRequest request, StreamObserver<TaxiRideProto.RideRequest> responseObserver) {
            TaxiRideProto.City source = request.getStartLocation();
            TaxiRideProto.City dst = request.getEndLocation();
            TaxiRideProto.Date date = request.getDate();
            LocalDate local_date = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            Ride ride = new Ride(request.getFirstName(),
                    request.getLastName(),
                    request.getPhoneNumber(),
                    new City(source),
                    new City(dst),
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
            LocalDate local_date = protoUtils.getDateFromProto(request.getDate());
            ArrayList<City> path = new ArrayList<>();
            for (TaxiRideProto.City city : request.getCityPathList()) {
                path.add(new City(city));
            }
            User user = new User(new City(source),
                    request.getFirstName(),
                    request.getLastName(),
                    local_date,
                    path);

            // TODO : remove - debug use only

            System.out.println(user);

            // For now: let assume only one city in path: path = [end], and we want src -> end @ date
            City end_city = user.getCities_in_path().get(0);

            List<String> all_cities = zkService.getLiveNodes("");
            all_cities.remove(source.getCityName());
            System.out.println(all_cities);
            TaxiRideProto.DriveRequest driveRequest = TaxiRideProto.DriveRequest
                    .newBuilder()
                    .setSource(source)
                    .setDst(protoUtils.getProtoFromCity(end_city))
                    .setDate(protoUtils.getProtoFromDate(local_date))
                    .build();

            // for each city that we want to ask for path:
            for (String city_name : all_cities) {
                String[] city_server_details = zkService.getLiveNodes(city_name).get(0).split(":");
                ManagedChannel channel = ManagedChannelBuilder
                        .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                        .usePlaintext()
                        .build();
                TaxiServiceGrpc.TaxiServiceBlockingStub stub = TaxiServiceGrpc.newBlockingStub(channel);
                try {
                    Iterator<TaxiRideProto.RideRequest> response = stub.path(driveRequest);
                    for (int i = 1; response.hasNext(); i++) {
                        TaxiRideProto.RideRequest drive = response.next();
                        System.out.println("Start : " + drive.getStartLocation().getCityName()+"\nEnd : "
                                + drive.getEndLocation().getCityName() + "\n pd : " + drive.getPd());
                        System.out.println("-------------");
                    }
                    System.out.println(response);
                } catch (StatusRuntimeException e) {
                    e.printStackTrace();
                    System.out.println("server " + city_name + " took to long to respond");
                }
            }
        }


        @Override
        public void path(TaxiRideProto.DriveRequest request, StreamObserver<TaxiRideProto.RideRequest> responseObserver) {
            City source = new City(request.getSource());
            City dst = new City(request.getDst());
            TaxiRideProto.Date date = request.getDate();
            LocalDate ride_date = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            for (Ride ride_in_repo : rideRepository.findAll()) {
                if (ride_in_repo.in_deviation_distance(source) && ride_in_repo.in_deviation_distance(dst) &&
                        ride_in_repo.getDate().equals(ride_date) && ride_in_repo.getVacancies() > 0) {
                    TaxiRideProto.RideRequest rideRequest = TaxiRideProto.RideRequest
                            .newBuilder()
                            .setId(ride_in_repo.getId())
                            .setFirstName(ride_in_repo.getFirst_name())
                            .setLastName(ride_in_repo.getLast_name())
                            .setPd(ride_in_repo.getPd())
                            .setPhoneNumber(ride_in_repo.getPhone_number())
                            .setVacancies(ride_in_repo.getVacancies())
                            .setDate(protoUtils.getProtoFromDate(ride_date))
                            .setStartLocation(protoUtils.getProtoFromCity(ride_in_repo.getStart_location()))
                            .setEndLocation(protoUtils.getProtoFromCity(ride_in_repo.getEnd_location()))
                            .build();

                    responseObserver.onNext(rideRequest);
                }
            }
        responseObserver.onCompleted();
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
