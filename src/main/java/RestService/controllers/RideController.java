package RestService.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import TaxiRide.*;
import RestService.repository.*;

import java.util.List;

@Slf4j
@RestController
public class RideController {
    private final RideRepository repository;

    public RideController() {
        this.repository = new RideRepository();
    }

    @PostMapping("/rides")
    void post_ride(@RequestBody Ride new_ride){
        Ride newRide = repository.save(new_ride);
    }

    @GetMapping("/rides")
    List<Ride> get_all_rides(){
        return repository.findAll();
    }
}


