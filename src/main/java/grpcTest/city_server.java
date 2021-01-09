package grpcTest;

import TaxiRide.City;
import Utils.Errors;
import Utils.UserRepoInstance;
import Utils.protoUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.RepeatedFieldBuilder;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.zookeeper.KeeperException;
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
    private final String RIDE = "Ride";
    private final String USER = "User";
    private final int port;
    private RideRepository rideRepository;
    private UserRepository userRepository;
    private final Server server;
    private final String city;
    private final String function;
    public static ZkServiceImpl zkService;
    private final String ip_host;

    public city_server(int port, String city, String host, String function) throws IOException {
        this(ServerBuilder.forPort(port), port, city, host, function);
    }
    public city_server(ServerBuilder<?> serverBuilder, int port, String city, String host, String function) throws UnknownHostException {
        this.port = port;
        server = serverBuilder.addService(new TaxiImpl())
                .build();
        this.city = city;
        this.rideRepository = new RideRepository();
        this.userRepository = new UserRepository();
        this.function = function;
        this.ip_host = InetAddress.getLocalHost().getHostAddress() + ":" + port;

        zkService = new ZkServiceImpl(host);
        System.out.println("connected to host: " + host);

        zkService.createParentNode(null, function);
        System.out.println("(if needed) create the zk main folder");

        zkService.createParentNode(city, function);
        System.out.println("(if needed) create the zk city folder");

        zkService.addToLiveNodes(this.ip_host, this.ip_host, city, function);
        System.out.println("Znode added to zk sever");
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


        public void sendAllDuplicates(TaxiRideProto.UserRepoRequest userRepoRequest){
            List<String> duplicates = zkService.getLiveNodes(city, function);
            duplicates.remove(ip_host);

            for (String duplicate : duplicates){
                String[] city_server_details = duplicate.split(":");
                ManagedChannel channel = ManagedChannelBuilder
                        .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                        .usePlaintext()
                        .build();
                TaxiServiceGrpc.TaxiServiceFutureStub sendUser = TaxiServiceGrpc.newFutureStub(channel);
                sendUser.duplicateUser(userRepoRequest);
                try {
                    channel.awaitTermination(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    // maybe see if duplicate crached or something
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void user(TaxiRideProto.UserRequest request, StreamObserver<TaxiRideProto.DriveOptions> responseObserver) {
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

            UserRepoInstance newUser = userRepository.save(user);
            // TODO : remove - debug use only

            System.out.println(user);

            TaxiRideProto.UserRepoRequest userRepoRequest = protoUtils.getProtoFromUser(newUser).build();
            sendAllDuplicates(userRepoRequest);

            TaxiRideProto.DriveOptions.Builder res = TaxiRideProto.DriveOptions.newBuilder();
            ArrayList<Ride> foundRides = setPathCity(local_date, new City(source), path);
            if (foundRides != null) {
                for (Ride ride : foundRides) {
                    res.addRideOptions(protoUtils.getProtoFromRide(ride)).build();
                }
                responseObserver.onNext(res.build());
                UserRepoInstance updatedUser = userRepository.changeStatusById(newUser.getId());
                TaxiRideProto.UserRepoRequest updatedUserRequest = protoUtils.getProtoFromUser(updatedUser).build();
                sendAllDuplicates(updatedUserRequest);

            } else {
                responseObserver.onNext(null);
            }
            responseObserver.onCompleted();
        }

        private ArrayList<Ride> setPathCity(LocalDate local_date, City source, ArrayList<City> path) {
            ArrayList<Long> chosenRides = new ArrayList<>();
            ArrayList<String> cityNames = new ArrayList<>();
            ArrayList<Ride> rides = new ArrayList<>();
            City currentCity = source;
            for (City pathCity : path) {
                boolean choseOptionForLeg = false;
                List<String> all_cities = null;
                try {
                    all_cities = zkService.getLiveNodes("", "");
                } catch (ZkNoNodeException e) {
                    System.out.println("No cities in system!");
                    return null;
                }
                // TODO: debug- remove
                System.out.println(all_cities);
                TaxiRideProto.DriveRequest driveRequest = TaxiRideProto.DriveRequest
                        .newBuilder()
                        .setSource(protoUtils.getProtoFromCity(currentCity))
                        .setDst(protoUtils.getProtoFromCity(pathCity))
                        .setDate(protoUtils.getProtoFromDate(local_date))
                        .build();

                for (String city_name : all_cities) {
                    ManagedChannel channel = null;
                    try {
                        List<String> servers = zkService.getLiveNodes(city_name, RIDE);
                        if (servers.size() == 0) {
                            continue;
                        }
                        String[] city_server_details = zkService.getLiveNodes(city_name, RIDE).get(0).split(":");
                        channel = ManagedChannelBuilder
                                .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                                .usePlaintext()
                                .build();
                        TaxiServiceGrpc.TaxiServiceBlockingStub stub = TaxiServiceGrpc.newBlockingStub(channel);
                        TaxiRideProto.DriveResponse response = stub.path(driveRequest);
                        long driveId = response.getDriveId();
                        if (driveId < 0) {
                            System.out.println(city_name + " id < 0");
                            continue;
                        }
                        chosenRides.add(driveId);
                        cityNames.add(response.getServerName());
                        rides.add(new Ride(response.getRide()));
                        System.out.println("-------------");
                        choseOptionForLeg = true;
                        break;
                    } catch (ZkNoNodeException e) {
                        System.out.println("No nodes : " + e);
                        continue;
                    } catch (StatusRuntimeException e) {
                        System.out.println("server " + city_name + " took to long to respond");
                    } finally {
                        if (channel != null) {
                            channel.shutdown();
                        }
                    }
                }
                if (!choseOptionForLeg) {
                    System.out.println("Current City : " + currentCity.getCity_name());
                    System.out.println("Dst City : " + pathCity.getCity_name());

                    System.out.println("Could not manage to book the trip!");
                    for (int i = 0; i < chosenRides.size(); i++) {
                        long it_driveId = chosenRides.get(i);
                        String it_server_name = cityNames.get(i);
                        String[] city_server_details;
                        try {
                            city_server_details = zkService.getLiveNodes(it_server_name, RIDE).get(0).split(":");
                        } catch (ZkNoNodeException e) {
                            System.out.println("Could not undo action because " + e);
                            return null;
                        }
                        ManagedChannel channel = ManagedChannelBuilder
                                .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                                .usePlaintext()
                                .build();
                        TaxiServiceGrpc.TaxiServiceFutureStub stubFuture = TaxiServiceGrpc.newFutureStub(channel);
                        TaxiRideProto.DriveResponse cancelPath = TaxiRideProto.DriveResponse
                                .newBuilder()
                                .setDriveId(it_driveId)
                                .setServerName("")
                                .setFlag(-1)
                                .build();
                        stubFuture.cancelPath(cancelPath);
                        try {
                            channel.awaitTermination(500, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            System.out.println("Serever " + it_server_name + " took to long to responde");
                        }
                    }

                    return null;
                } else {
                    currentCity = pathCity;

                }

                System.out.println("Managed to find path!");
                System.out.println(cityNames);
                return rides;
            }
            return null;
        }

        @Override
        public void duplicateUser(TaxiRideProto.UserRepoRequest request, StreamObserver<TaxiRideProto.UserRepoRequest> responseObserver) {
            System.out.println(request);
            TaxiRideProto.UserRequest userRequest = request.getUser();
            TaxiRideProto.City source = userRequest.getLocation();
            LocalDate local_date = protoUtils.getDateFromProto(userRequest.getDate());
            ArrayList<City> path = new ArrayList<>();
            for (TaxiRideProto.City city : userRequest.getCityPathList()) {
                path.add(new City(city));
            }
            UserRepoInstance newUserRepoInstance = new UserRepoInstance(new City(source),
                    userRequest.getFirstName(),
                    userRequest.getLastName(),
                    local_date,
                    path,
                    UserRepoInstance.UserStatus.values()[request.getStatus()]);
            userRepository.save(newUserRepoInstance);
        }

        @Override
        public void path(TaxiRideProto.DriveRequest request, StreamObserver<TaxiRideProto.DriveResponse> responseObserver) {

            City source = new City(request.getSource());
            City dst = new City(request.getDst());
            TaxiRideProto.Date date = request.getDate();
            LocalDate ride_date = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            boolean legalRide = false;
            for (Ride ride_in_repo : rideRepository.findAll()) {
                if (ride_in_repo.in_deviation_distance(source) && ride_in_repo.in_deviation_distance(dst) &&
                        ride_in_repo.getDate().equals(ride_date) && ride_in_repo.getVacancies() > 0) {
                    try {
                        ride_in_repo.lowerVacancies();
                        TaxiRideProto.DriveResponse rideResponse = TaxiRideProto.DriveResponse
                                .newBuilder()
                                .setDriveId(ride_in_repo.getId())
                                .setServerName(city)
                                .setRide(protoUtils.getProtoFromRide(ride_in_repo))
                                .build();
                        responseObserver.onNext(rideResponse);
                        legalRide = true;
                        break;
                    } catch (Errors.FullRide fullRide) {
//                        fullRide.printStackTrace();
                        continue;
                    }
                }
            }
            if (!legalRide)
            {
                responseObserver.onNext(TaxiRideProto.DriveResponse.newBuilder()
                .setDriveId(-1)
                .setServerName("")
                .setFlag(-1)
                .build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void cancelPath(TaxiRideProto.DriveResponse request, StreamObserver<TaxiRideProto.DriveResponse> responseObserver) {
            try {
                rideRepository.getRide(request.getDriveId()).upVacancies();
            } catch (Errors.RideNotExists rideNotExists) {
                rideNotExists.printStackTrace();
                System.out.println("Major Error! Tried to up vacancies in ride with id + " + request.getDriveId() +
                        " but drive does not exists in city " + city +" !\n");
            }
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
