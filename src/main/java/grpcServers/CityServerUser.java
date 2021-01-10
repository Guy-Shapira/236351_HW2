package grpcServers;

import TaxiRide.City;
import Utils.Errors;
import Utils.FoundRoute;
import Utils.UserRepoInstance;
import Utils.protoUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import TaxiRide.Ride;
import TaxiRide.User;
import RestService.repository.*;
import zookeeper.Member;
import zookeeper.ZkServiceImpl;
import java.net.InetAddress;


public class CityServerUser {
    private final String RIDE = "Ride";
    private final String USER = "User";
    private final int port;
    private UserRepository userRepository;
    private final Server server;
    private final String city;
    public static ZkServiceImpl zkService;
    private final String ip_host;

    public CityServerUser(int port, String city, String host) throws IOException {
        this(ServerBuilder.forPort(port), port, city, host);
    }
    public CityServerUser(ServerBuilder<?> serverBuilder, int port, String city, String host) throws UnknownHostException {
        this.port = port;
        server = serverBuilder.addService(new TaxiImpl())
                .build();
        this.city = city;
        this.userRepository = new UserRepository();;
        this.ip_host = InetAddress.getLocalHost().getHostAddress() + ":" + port;

        zkService = new ZkServiceImpl(host);
        System.out.println("connected to host: " + host);

        zkService.createParentNode(null, USER);
        System.out.println("(if needed) create the zk main folder");

        zkService.createParentNode(city, USER);
        System.out.println("(if needed) create the zk city folder");

        zkService.addToLiveNodes(this.ip_host, this.ip_host, city, USER);
        System.out.println("Znode added to zk sever");
        zkService.registerChildrenChangeWatcher(zkService.MEMBER + "/" + city, new Member());
        System.out.println("added watcher / listener");
    }

    class TaxiImpl extends TaxiServiceGrpc.TaxiServiceImplBase {
        public void sendAllDuplicates(TaxiRideProto.UserRepoRequest userRepoRequest) {
            List<String> duplicates = zkService.getLiveNodes(city, USER);
            duplicates.remove(ip_host);

            for (String duplicate : duplicates) {
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
            Utils.FoundRoute foundRides = setPathCity(newUser.getId(), local_date, new City(source), path);
            if (foundRides != null && foundRides.getRides() != null) {
                for (Ride ride : foundRides.getRides()) {
                    res.addRideOptions(protoUtils.getProtoFromRide(ride)).build();
                }
                responseObserver.onNext(res.build());

            } else {
                responseObserver.onNext(null);
            }
            UserRepoInstance updatedUser = userRepository.changeStatusById(newUser.getId());
            TaxiRideProto.UserRepoRequest updatedUserRequest = protoUtils.getProtoFromUser(updatedUser).build();
            sendAllDuplicates(updatedUserRequest);
            if (foundRides != null) {
                this.sendAck(newUser.getId(), foundRides);
            }
            responseObserver.onCompleted();
        }

        private void sendAck(long userId, Utils.FoundRoute foundRides) {
            Integer count_acks = 0;
            for (Ride ride : foundRides.getRides()) {
                TaxiRideProto.DriveResponse ackRequest = TaxiRideProto.DriveResponse
                        .newBuilder()
                        .setServerName(city)
                        .setUserId(userId)
                        .setDriveId(ride.getId())
                        .setFlag(-1)
                        .build();

                String[] city_server_details;
                try {
                    city_server_details = zkService.makeAndReturnLeaderForCity(ride.getStart_location().getCity_name(), RIDE).split(":");
                    ManagedChannel channel = ManagedChannelBuilder
                            .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                            .usePlaintext()
                            .build();
                    TaxiServiceGrpc.TaxiServiceFutureStub stubFuture = TaxiServiceGrpc.newFutureStub(channel);
                    stubFuture.completeReservation(ackRequest);
                    try {
                        channel.awaitTermination(500, TimeUnit.MILLISECONDS);
                        count_acks += 1;
                    } catch (InterruptedException e) {
                        System.out.println("Serever " + city_server_details + " took to long to respond");
                        this.cancelPath(userId, foundRides.getPrefix(count_acks));
                        return;

                    }
                } catch (Errors.MoreThenOneLeaderForTheCity | Errors.NoServerForCity leaderError) {
                    this.cancelPath(userId, foundRides.getPrefix(count_acks));
                    throw new RuntimeException("Could not find leader");
                }
            }
        }


        private FoundRoute setPathCity(long userId, LocalDate local_date, City source, ArrayList<City> path) {
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
                        .setUserCityName(city)
                        .setUserId(userId)
                        .build();

                for (String city_name : all_cities) {
                    ManagedChannel channel = null;
                    try {
                        List<String> servers = zkService.getLiveNodes(city_name, RIDE);
                        if (servers.size() == 0) {
                            continue;
                        }
                        String[] city_server_details = zkService.makeAndReturnLeaderForCity(city_name, RIDE).split(":");
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
                        rides.add(new Ride(response.getRide(), response.getRide().getId()));
                        System.out.println("-------------");
                        choseOptionForLeg = true;
                        break;
                    } catch (ZkNoNodeException e) {
                        System.out.println("No nodes : " + e);
                        continue;
                    } catch (StatusRuntimeException e) {
                        System.out.println("server " + city_name + " took to long to respond");
                    } catch (Errors.MoreThenOneLeaderForTheCity | Errors.NoServerForCity leaderError) {
                        throw new RuntimeException("Error founding leader");
                    } finally {
                        if (channel != null) {
                            channel.shutdown();
                        }
                    }
                }
                if (!choseOptionForLeg) {
                    this.cancelPath(userId, new FoundRoute(chosenRides, rides, cityNames));
                    return null;

                } else {
                    currentCity = pathCity;
                }
            }
            System.out.println("Managed to find path!");
            System.out.println(cityNames);
            return new FoundRoute(chosenRides, rides, cityNames);
        }

        private void cancelPath(long userId, FoundRoute path) {
            System.out.println("Could not manage to book the trip!");
            for (
                int i = 0; i < path.getDriveIds().size(); i++) {
                long it_driveId = path.getDriveIds().get(i);
                String it_server_name = path.getCityNames().get(i);
                String[] city_server_details = new String[0];
                try {
                    city_server_details = zkService.makeAndReturnLeaderForCity(it_server_name, RIDE).split(":");

                }catch (Errors.MoreThenOneLeaderForTheCity | Errors.NoServerForCity leaderError) {
                    System.out.println("Could not undo action because " + leaderError);
                }
                ManagedChannel channel = ManagedChannelBuilder
                        .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                        .usePlaintext()
                        .build();

                TaxiServiceGrpc.TaxiServiceFutureStub stubFuture = TaxiServiceGrpc.newFutureStub(channel);
                TaxiRideProto.DriveResponse cancelPath = TaxiRideProto.DriveResponse
                        .newBuilder()
                        .setDriveId(it_driveId)
                        .setServerName(city)
                        .setUserId(userId)
                        .setFlag(-1)
                        .build();
                stubFuture.cancelPath(cancelPath);
                try {
                    channel.awaitTermination(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    System.out.println("Serever " + it_server_name + " took to long to responde");
                }
            }
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
                    CityServerUser.this.stop();
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
