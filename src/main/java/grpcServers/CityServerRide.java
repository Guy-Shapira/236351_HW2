package grpcServers;

import RestService.repository.RideRepository;
import TaxiRide.City;
import TaxiRide.Ride;
import Utils.Errors;
import Utils.RideRepoInstance;
import Utils.protoUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;
import zookeeper.ZkServiceImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CityServerRide {
    private final String RIDE = "Ride";
    private final String USER = "User";
    private final int port;
    private RideRepository rideRepository;
    private final Server server;
    private final String city;
    public static ZkServiceImpl zkService;
    private final String ip_host;

    public CityServerRide(int port, String city, String host) throws IOException {
        this(ServerBuilder.forPort(port), port, city, host);
    }
    public CityServerRide(ServerBuilder<?> serverBuilder, int port, String city, String host) throws UnknownHostException {
        this.port = port;
        server = serverBuilder.addService(new TaxiImpl())
                .build();
        this.city = city;
        this.rideRepository = new RideRepository();
        this.ip_host = InetAddress.getLocalHost().getHostAddress() + ":" + port;

        zkService = new ZkServiceImpl(host);
        System.out.println("connected to host: " + host);

        zkService.createParentNode(null, RIDE);
        System.out.println("(if needed) create the zk main folder");

        zkService.createParentNode(city, RIDE);
        System.out.println("(if needed) create the zk city folder");

        zkService.addToLiveNodes(this.ip_host, this.ip_host, city, RIDE);
        System.out.println("Znode added to zk sever");

        this.getSnapShot();
    }


    public void getSnapShot(){
        // if leader do nothing, else ask leader for snapshot and store to repo
        String[] currentLeader = null;
        try {
            currentLeader = zkService.makeAndReturnLeaderForCity(city, RIDE).split(":");
        } catch (Errors.MoreThenOneLeaderForTheCity | Errors.NoServerForCity leaderError) {
            leaderError.printStackTrace();
            System.out.println("Could not find a leader");
            throw new RuntimeException("No leader, cant operate");
        }
        String[] server_details = ip_host.split(":");
        if (currentLeader[0].equals(server_details[0]) && currentLeader[1].equals(server_details[1])){
            // is leader, nothing to update
            System.out.println("Is leader, no update needed");
            return;
        }else {
            System.out.println("Is not leader, update needed");
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(currentLeader[0], Integer.parseInt(currentLeader[1]))
                    .usePlaintext()
                    .build();

            TaxiRideProto.EmptyMessage emptyMessage = TaxiRideProto.EmptyMessage.newBuilder().build();
            TaxiServiceGrpc.TaxiServiceBlockingStub stub = TaxiServiceGrpc.newBlockingStub(channel);
            TaxiRideProto.RideSnapshot response = stub.getRideSnapshot(emptyMessage);
            RideRepository newRepo = protoUtils.getRideRepoFromProto(response);
            rideRepository.setIdIndex(newRepo.getIdIndex());
            rideRepository.setRides(newRepo.getRides());
            System.out.println(rideRepository.getRides());
        }


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
            rideRepository.cleanRepo();
            RideRepoInstance newRide = rideRepository.save(ride);
            sendAllDuplicates(newRide);

        }

        @Override
        public void path(TaxiRideProto.DriveRequest request, StreamObserver<TaxiRideProto.DriveResponse> responseObserver) {
            City source = new City(request.getSource());
            City dst = new City(request.getDst());
            TaxiRideProto.Date date = request.getDate();
            LocalDate ride_date = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            boolean legalRide = false;
            for (RideRepoInstance ride_in_repo : rideRepository.findAll()) {
                if (ride_in_repo.in_deviation_distance(source) && ride_in_repo.in_deviation_distance(dst) &&
                        ride_in_repo.getDate().equals(ride_date) && ride_in_repo.getVacancies() > 0) {
                    try {
                        ride_in_repo.lowerVacancies(request.getUserCityName(), request.getUserId());
                        TaxiRideProto.DriveResponse rideResponse = TaxiRideProto.DriveResponse
                                .newBuilder()
                                .setDriveId(ride_in_repo.getId())
                                .setServerName(city)
                                .setRide(protoUtils.getProtoFromRideWithId(ride_in_repo))
                                .build();
                        responseObserver.onNext(rideResponse);
                        legalRide = true;
                        sendAllDuplicates(ride_in_repo);
                        break;
                    } catch (Errors.FullRide | Errors.AlreadyReservedTheRide  errorInReservingRide) {
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
            System.out.println("Canceled");
            try {
                String key = request.getServerName() + ":" + request.getUserId();
                rideRepository.getRide(request.getDriveId()).upVacancies(key);

                sendAllDuplicates(rideRepository.getRide(request.getDriveId()));
            } catch (Errors.RideNotExists rideNotExists) {
                rideNotExists.printStackTrace();
                System.out.println("Major Error! Tried to up vacancies in ride with id + " + request.getDriveId() +
                        " but drive does not exists in city " + city +" !\n");
            }
        }

        @Override
        public void completeReservation(TaxiRideProto.DriveResponse request, StreamObserver<TaxiRideProto.DriveResponse> responseObserver) {
            System.out.println("Ack");
            try {
                String key = request.getServerName() + ":" + request.getUserId();
                rideRepository.getRide(request.getDriveId()).deleteTimeStamp(key);

                // update dups
            } catch (Errors.RideNotExists rideNotExists) {
                rideNotExists.printStackTrace();
                System.out.println("Major Error! Tried to up remove timestamp in ride with id + " + request.getDriveId() +
                        " but drive does not exists in city " + city +" !\n");
            }
        }

        public void sendAllDuplicates (RideRepoInstance ride){
            TaxiRideProto.RideRepoRequest rideRepoRequest = protoUtils.getProtoFromRideRepo(ride).build();

            List<String> duplicates = zkService.getLiveNodes(city, RIDE);
            duplicates.remove(ip_host);

            for (String duplicate : duplicates) {
                String[] city_server_details = duplicate.split(":");
                ManagedChannel channel = ManagedChannelBuilder
                        .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                        .usePlaintext()
                        .build();
                TaxiServiceGrpc.TaxiServiceFutureStub sendUser = TaxiServiceGrpc.newFutureStub(channel);
                sendUser.duplicateRide(rideRepoRequest);
                try {
                    channel.awaitTermination(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    // maybe see if duplicate crached or something
                    e.printStackTrace();
                }
            }
        }
        // dup function!
        @Override
        public void duplicateRide(TaxiRideProto.RideRepoRequest request, StreamObserver<TaxiRideProto.RideRepoRequest> responseObserver) {
            Ride ride = new Ride(request.getRide(), request.getRide().getId());
            Map<String, Long> rideMap = request.getHashmapMap();
            System.out.println("RIDE: " + request.getRide() + "\n\n\n\n");
            try {
                rideRepository.getRide(ride.getId()).setTimestamps(rideMap);
            } catch (Errors.RideNotExists rideNotExists) {
                RideRepoInstance savedRide = rideRepository.save(ride);
                try {
                    rideRepository.getRide(savedRide.getId()).setTimestamps(rideMap);
                } catch (Errors.RideNotExists notExists) {
                    notExists.printStackTrace();
                }
            }
        }

        @Override
        public void getRideSnapshot(TaxiRideProto.EmptyMessage request, StreamObserver<TaxiRideProto.RideSnapshot> responseObserver) {
            responseObserver.onNext(protoUtils.getProtoFromRidesSnapshot(rideRepository).build());
            responseObserver.onCompleted();
        }

        @Override
        public void leaderCancels(TaxiRideProto.cancelMessage request, StreamObserver<TaxiRideProto.cancelMessage> responseObserver) {
            long userId = request.getId();
            String key = request.getCityName() + ":" + userId;
            for (RideRepoInstance ride : rideRepository.findAll()){
                ride.upVacancies(key);
                sendAllDuplicates(ride);
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
                    CityServerRide.this.stop();
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
