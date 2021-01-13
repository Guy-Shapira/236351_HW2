package RestService.controllers;

import RestService.repository.RideRepository;
import RestService.repository.UserRepository;
import TaxiRide.City;
import TaxiRide.Ride;
import TaxiRide.User;
import Utils.Errors;
import Utils.UserRepoInstance;
import Utils.protoUtils;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static RestService.main.zkService;

@Slf4j
@RestController
public class SnapshotController {
    private ArrayList<UserRepository> userRepo;
    private ArrayList<RideRepository> rideRepo;
    private final String RIDE = "Ride";
    private final String USER = "User";

    public SnapshotController() {
        // TOOD: remove - for debug only
        this.userRepo = new ArrayList<>();
        this.rideRepo = new ArrayList<>();
    }

    @GetMapping("/state")
    void get_state() {
        List<String> functions = new ArrayList<>();
        functions.add(RIDE);
        functions.add(USER);
        List<String> cities = zkService.getLiveNodes("", "");
        for (String city : cities) {
            for (String function : functions) {
                try {
                    String[] leaderForCity = zkService.makeAndReturnLeaderForCity(city, function).split(":");
                    ManagedChannel channel = ManagedChannelBuilder
                            .forAddress(leaderForCity[0], Integer.parseInt(leaderForCity[1]))
                            .usePlaintext()
                            .build();
                    TaxiServiceGrpc.TaxiServiceFutureStub stub = TaxiServiceGrpc.newFutureStub(channel);

                    if (function.equals(RIDE)) {
                        ListenableFuture<TaxiRideProto.RideSnapshot> snapshot = stub.getRideSnapshot(TaxiRideProto.EmptyMessage.newBuilder().build());
                        rideRepo.add(protoUtils.getRideRepoFromProto(snapshot.get()));
                        System.out.println(rideRepo);

                    }else{
                        ListenableFuture<TaxiRideProto.UserSnapshot> snapshot = stub.getUserSnapshot(TaxiRideProto.EmptyMessage.newBuilder().build());
                        userRepo.add(protoUtils.getUserRepoFromProto(snapshot.get()));
                        System.out.println(userRepo);

                    }
                } catch (Errors.MoreThenOneLeaderForTheCity | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }catch (Errors.NoServerForCity noServerForCity){
                    System.out.println("No leader for city " + city + " with function " + function);
                }


            }
        }
    }
}
