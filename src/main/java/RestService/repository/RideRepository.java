package RestService.repository;
import TaxiRide.Ride;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import Utils.Errors;


public class RideRepository {
    private ArrayList<Ride> rides;
    private long idIndex = 1;

    public RideRepository(){
        rides = new ArrayList<>();
    }

    public List<Ride> findAll(){
        return rides;
    }

    public Ride save(Ride newRide){
        if (rides.contains(newRide)){
            // TODO: throw better exception
            throw new RuntimeException("already exists");
        }
        newRide.setId(idIndex ++);
        rides.add(newRide);
        return newRide;
    }

    public void delete(Ride ride){
        rides.remove(ride);
    }

    public Ride getRide (long rideId) throws Errors.RideNotExists {
        for (Ride ride : this.rides){
            if (ride.getId().equals(rideId)){
                return ride;
            }
        }
        throw new Errors.RideNotExists();
    }

}
