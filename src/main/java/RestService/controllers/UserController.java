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

@Slf4j
@RestController
public class UserController {
    private final UserRepository repository;
    private TaxiServiceGrpc.TaxiServiceFutureStub stub;
    private ManagedChannel channel;

    public UserController()
    {
        this.channel = ManagedChannelBuilder.forAddress("127.0.0.1", 8081)
                .usePlaintext()
                .build();
        this.repository = new UserRepository();
        this.stub = TaxiServiceGrpc.newFutureStub(channel);
    }
    @PostMapping("/users")
    void post_ride(@RequestBody User new_user){
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
        stub.user(user);


    }

    @GetMapping("/users")
    List<User> get_all_rides(){
        return repository.findAll();
    }
}


