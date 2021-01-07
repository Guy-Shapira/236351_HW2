package RestService.controllers;

import RestService.repository.RideRepository;
import RestService.repository.UserRepository;
import TaxiRide.City;
import TaxiRide.User;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static RestService.main.zkService;

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
    void post_ride(@RequestBody User new_user) throws InterruptedException {

        // TODO: remove, only here to debug stuff
        User newUser = repository.save(new_user);


        City user_city = new_user.getLocation();

        TaxiRideProto.UserRequest user = TaxiRideProto.UserRequest
                .newBuilder()
                .setId(new_user.getId())
                .setFirstName(new_user.getFirst_name())
                .setLastName(new_user.getLast_name())
                .setLocation(TaxiRideProto.City
                        .newBuilder()
                        .setCityId(user_city.getCity_id())
                        .setCityName(user_city.getCity_name())
                        .setX(user_city.getX())
                        .setY(user_city.getY())
                ).build();

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
        stub.user(user);
        channel.awaitTermination(3, TimeUnit.SECONDS);



    }

    // TODO: remove, only here to debug stuff
    @GetMapping("/users")
    List<User> get_all_rides(){
        return repository.findAll();
    }
}


