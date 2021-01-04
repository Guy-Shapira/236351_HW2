package RestService.controllers;

import RestService.repository.UserRepository;
import TaxiRide.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserRepository repository;

    public UserController() {
        this.repository = new UserRepository();
    }

    @PostMapping("/users")
    void post_ride(@RequestBody User new_user){
        User newRide = repository.save(new_user);
    }

    @GetMapping("/users")
    List<User> get_all_rides(){
        return repository.findAll();
    }
}


