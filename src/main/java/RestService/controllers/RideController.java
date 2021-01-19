package RestService.controllers;

import Utils.Errors;
import Utils.RideRepoInstance;
import com.google.protobuf.TextFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.grpc.StatusRuntimeException;
import TaxiRide.*;
import RestService.repository.*;
import org.springframework.web.server.ResponseStatusException;
import protos.TaxiRideProto;
import protos.TaxiServiceGrpc;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static RestService.main.zkService;
import Utils.protoUtils;


@Slf4j
@RestController
@RequestMapping

public class RideController {
    private final RideRepository repository;
    private static final String FUNCTION = "Ride";

    public RideController()
    {
        this.repository = new RideRepository();
    }

    String send_ride(City src_city, TaxiRideProto.RideRequest ride, Integer count){
        if (count >= 5){
            return "An error occurred, please try again later!";
        }
        try {
            String cityRideLeader = zkService.makeAndReturnLeaderForCity(src_city.getCity_name(), FUNCTION);
            System.out.println("Found " + cityRideLeader + " as leader for function" + FUNCTION);
            String[] city_server_details = cityRideLeader.split(":");
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                    .usePlaintext()
                    .build();

            // TODO: maybe change to blocking?
            TaxiServiceGrpc.TaxiServiceFutureStub stub = TaxiServiceGrpc.newFutureStub(channel);
            stub.ride(ride);
            channel.awaitTermination(500, TimeUnit.MILLISECONDS);
            channel.shutdown();
            return "Your ride was posted! Have a safe ride";

        } catch (Errors.MoreThenOneLeaderForTheCity | Errors.NoServerForCity leaderError){
            System.out.println("Could not find server for wanted city " + src_city.getCity_name());
            return "An error occurred, please try again later!";
        } catch (InterruptedException e) {
            return send_ride(src_city, ride, count + 1);
        }
    }

    @PostMapping("/rides")
    String post_ride(@RequestBody Ride new_ride)  {

        // TODO: remove, only here to debug stuff
        Ride newRide = repository.save(new_ride);


        LocalDate ride_date = new_ride.getDate();
        City src_city = new_ride.getStart_location();
        City end_city = new_ride.getEnd_location();

        TaxiRideProto.RideRequest ride = TaxiRideProto.RideRequest
                .newBuilder()
                .setId(newRide.getId())
                .setFirstName(new_ride.getFirst_name())
                .setLastName(new_ride.getLast_name())
                .setPd(new_ride.getPd())
                .setPhoneNumber(new_ride.getPhone_number())
                .setVacancies(new_ride.getVacancies())
                .setDate(protoUtils.getProtoFromDate(ride_date))
                .setEndLocation(protoUtils.getProtoFromCity(end_city))
                .setStartLocation(protoUtils.getProtoFromCity(src_city))
                .build();
        return send_ride(src_city, ride, 0);

        // user grpc to send ride to all servers
    }

    // TODO: remove, only here to debug stuff
    @GetMapping("/rides")
    List<RideRepoInstance> get_all_rides(){
        return repository.findAll();
    }
}


