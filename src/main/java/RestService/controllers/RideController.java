package RestService.controllers;

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

    public RideController()
    {
        this.repository = new RideRepository();
    }

    @PostMapping("/rides")
    void post_ride(@RequestBody Ride new_ride) throws InterruptedException {

        // TODO: remove, only here to debug stuff
        Ride newRide = repository.save(new_ride);


        LocalDate ride_date = new_ride.getDate();
        City src_city = new_ride.getStart_location();
        City end_city = new_ride.getEnd_location();

        TaxiRideProto.RideRequest ride = TaxiRideProto.RideRequest
                .newBuilder()
                .setId(new_ride.getId())
                .setFirstName(new_ride.getFirst_name())
                .setLastName(new_ride.getLast_name())
                .setPd(new_ride.getPd())
                .setPhoneNumber(new_ride.getPhone_number())
                .setVacancies(new_ride.getVacancies())
                .setDate(protoUtils.getProtoFromDate(ride_date))
                .setEndLocation(protoUtils.getProtoFromCity(end_city))
                .setStartLocation(protoUtils.getProtoFromCity(src_city))
                .build();

        List<String> city_servers  = zkService.getLiveNodes(src_city.getCity_name());
        System.out.println(city_servers);
        if (city_servers.size() == 0){
            throw new RuntimeException("No server in zookeeper system for city : " + src_city.getCity_name());
        }
        String[] city_server_details = city_servers.get(0).split(":");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(city_server_details[0], Integer.parseInt(city_server_details[1]))
                .usePlaintext()
                .build();
        System.out.println(channel);

        // TODO: maybe change to blocking?
        TaxiServiceGrpc.TaxiServiceFutureStub stub = TaxiServiceGrpc.newFutureStub(channel);
        stub.ride(ride);
        channel.awaitTermination(3, TimeUnit.SECONDS);

        // user grpc to send ride to all servers
    }

    // TODO: remove, only here to debug stuff
    @GetMapping("/rides")
    List<Ride> get_all_rides(){
        return repository.findAll();
    }
}


