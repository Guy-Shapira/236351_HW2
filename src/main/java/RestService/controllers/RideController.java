package RestService.controllers;

import com.google.protobuf.TextFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.grpc.StatusRuntimeException;
import TaxiRide.*;
import RestService.repository.*;
import org.springframework.web.server.ResponseStatusException;
import protos.TaxiRideProto;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class RideController {
    private final RideRepository repository;

    public RideController() {
        this.repository = new RideRepository();
    }

    @PostMapping("/rides")
    void post_ride(@RequestBody Ride new_ride) {
        Ride newRide = repository.save(new_ride);
        LocalDate ride_date = new_ride.getDate();
        City src_city = new_ride.getStart_location();
        City end_city = new_ride.getEnd_location();

        TaxiRideProto.RideRequest ride = TaxiRideProto.RideRequest
                .newBuilder()
                .setId(new_ride.getId())
                .setFirstName(new_ride.getFirst_name())
                .setLastName(new_ride.getLast_name())
                .setDate(TaxiRideProto.Date.
                        newBuilder()
                        .setDay(ride_date.getDayOfMonth())
                        .setMonth(ride_date.getMonthValue())
                        .setYear(ride_date.getYear()))
                .setEndLocation(TaxiRideProto.City
                        .newBuilder()
                        .setCityId(end_city.getCity_id())
                        .setCityName(end_city.getCity_name())
                        .setX(end_city.getX())
                        .setY(end_city.getY())
                )
                .setStartLocation(TaxiRideProto.City
                        .newBuilder()
                        .setCityId(src_city.getCity_id())
                        .setCityName(src_city.getCity_name())
                        .setX(src_city.getX())
                        .setY(src_city.getY())
                ).build();

        // user grpc to send ride to all servers
    }

    @GetMapping("/rides")
    List<Ride> get_all_rides(){
        return repository.findAll();
    }
}


