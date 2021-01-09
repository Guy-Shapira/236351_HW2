package RestService.controllers;

import RestService.repository.RideRepository;
import RestService.repository.UserRepository;
import TaxiRide.City;
import TaxiRide.Ride;
import TaxiRide.User;
import Utils.protoUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static RestService.main.zkService;
import Utils.protoUtils.*;

@Slf4j
@RestController
public class UserController {
    private final UserRepository repository;
    public UserController()
    {
        // TOOD: remove - for debug only
        this.repository = new UserRepository();
    }

    @PostMapping("/users")
    void post_ride(@RequestBody User new_user) {

        // TODO: remove, only here to debug stuff
        User newUser = repository.save(new_user);

        City user_city = new_user.getLocation();
        TaxiRideProto.UserRequest.Builder user_builder = TaxiRideProto.UserRequest
                .newBuilder()
                .setId(new_user.getId())
                .setFirstName(new_user.getFirst_name())
                .setLastName(new_user.getLast_name())
                .setLocation(protoUtils.getProtoFromCity(user_city))
                .setDate(protoUtils.getProtoFromDate(new_user.getDate()));


        for (City city : new_user.getCities_in_path()){
            user_builder.addCityPath(protoUtils.getProtoFromCity(city))
                    .build();
        }

        TaxiRideProto.UserRequest user = user_builder.build();
        System.out.println(user);
        List<String> city_servers  = zkService.getLiveNodes(user_city.getCity_name());
        if (city_servers.size() == 0){
            throw new RuntimeException("No server in zookeeper system for city : " + user_city.getCity_name());
        }
        String[] city_server_details = city_servers.get(0).split(":");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                .usePlaintext()
                .build();

        // TODO: maybe change to blocking?
        TaxiServiceGrpc.TaxiServiceFutureStub stub = TaxiServiceGrpc.newFutureStub(channel);
        ListenableFuture<TaxiRideProto.DriveOptions> response = stub.user(user);
        try {
            List<Ride> pathRides = new ArrayList<>();
            if (response == null)
            {
                System.out.println("Could not find a path!");
            }else {
                for (TaxiRideProto.RideRequest driveLeg : response.get().getRideOptionsList()) {
                    pathRides.add(new Ride(driveLeg));
                }
                System.out.println(response.get().getRideOptionsList());
                channel.awaitTermination(500, TimeUnit.MILLISECONDS);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    // TODO: remove, only here to debug stuff
    @GetMapping("/users")
    List<User> get_all_rides(){
        return repository.findAll();
    }
}


