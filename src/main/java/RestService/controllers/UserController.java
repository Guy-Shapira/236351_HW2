package RestService.controllers;

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
public class UserController {
    private final String FUNCTION = "User";
    private final UserRepository repository;
    public UserController()
    {
        // TOOD: remove - for debug only
        this.repository = new UserRepository();
    }

    @PostMapping("/users")
    String post_ride(@RequestBody User new_user) {

        User newUser = repository.save(new_user);

        City user_city = new_user.getLocation();
        TaxiRideProto.UserRequest.Builder user_builder = TaxiRideProto.UserRequest
                .newBuilder()
                .setId(newUser.getId())
                .setFirstName(newUser.getFirst_name())
                .setLastName(newUser.getLast_name())
                .setLocation(protoUtils.getProtoFromCity(user_city))
                .setDate(protoUtils.getProtoFromDate(newUser.getDate()));


        for (City city : new_user.getCities_in_path()){
            user_builder.addCityPath(protoUtils.getProtoFromCity(city))
                    .build();
        }

        TaxiRideProto.UserRequest user = user_builder.build();
        System.out.println(user);
        Boolean newLeaderFlag = false;
        String city_server = null;
        try {
            if (zkService.checkIfNewLeaderNeeded(user_city.getCity_name(), FUNCTION)){
                newLeaderFlag = true;
            }
            city_server = zkService.makeAndReturnLeaderForCity(user_city.getCity_name(), FUNCTION);
        } catch (Errors.MoreThenOneLeaderForTheCity | Errors.NoServerForCity leaderError){
            System.out.println("Could not find server for wanted city " + user_city.getCity_name());
            return "An error occurred, please try again later!";
        }
        String[] city_server_details = city_server.split(":");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                .usePlaintext()
                .build();

        if (newLeaderFlag) {
            TaxiServiceGrpc.TaxiServiceBlockingStub blockingStub = TaxiServiceGrpc.newBlockingStub(channel);
            blockingStub.setAsLeader(TaxiRideProto.EmptyMessage.newBuilder().build());
        }

        TaxiServiceGrpc.TaxiServiceFutureStub stub = TaxiServiceGrpc.newFutureStub(channel);
        ListenableFuture<TaxiRideProto.DriveOptions> response = stub.user(user);
        try {
            List<Ride> pathRides = new ArrayList<>();
            List<TaxiRideProto.RideRequest> path = response.get().getRideOptionsList();
            if (path.size() == 0)
            {
                System.out.println("Could not find a path!");
                return "Sorry, we could not find a path for you...";
            }else {
                for (TaxiRideProto.RideRequest driveLeg : path) {
                    pathRides.add(new Ride(driveLeg));
                }
                System.out.println(response.get().getRideOptionsList());
                channel.awaitTermination(500, TimeUnit.MILLISECONDS);
                return response.get().getRideOptionsList().toString();
            }

        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
            return "An error occurred, please try again later!";
        }
    }

    @GetMapping("/users")
    List<UserRepoInstance> get_all_rides(){
        return repository.findAll();
    }
}


