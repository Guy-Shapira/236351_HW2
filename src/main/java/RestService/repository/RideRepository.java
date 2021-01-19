package RestService.repository;
import TaxiRide.Ride;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import Utils.Errors;
import Utils.RideRepoInstance;
import Utils.UserRepoInstance;


public class RideRepository {
    private ArrayList<RideRepoInstance> rides;
    private long idIndex = 1;

    public RideRepository(){
        rides = new ArrayList<>();
    }

    public List<RideRepoInstance> findAll(){
        return rides;
    }

    public RideRepoInstance save(Ride newRide){
        if (rides.contains(newRide)){
            throw new RuntimeException("already exists");
        }
        RideRepoInstance newRideRepo;
        if (!(newRide instanceof RideRepoInstance)) {
            newRideRepo = new RideRepoInstance(newRide);
        }else {
            newRideRepo = (RideRepoInstance) newRide;
        }

        newRideRepo.setId(idIndex ++);
        rides.add(newRideRepo);
        return newRideRepo;
    }

    public void delete(Ride ride){
        rides.remove(ride);
    }

    public RideRepoInstance getRide (long rideId) throws Errors.RideNotExists {
        for (RideRepoInstance ride : this.rides){
            if (ride.getId().equals(rideId)){
                return ride;
            }
        }
        throw new Errors.RideNotExists();
    }

    public void cleanRepo(){
        long startTime = System.currentTimeMillis();
        for (RideRepoInstance ride : this.rides){
            ride.cleanupReservations(startTime);
        }
    }

    public ArrayList<RideRepoInstance> getRides() {
        return rides;
    }

    public void setRides(ArrayList<RideRepoInstance> rides) {
        this.rides = rides;
    }

    public long getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(long idIndex) {
        this.idIndex = idIndex;
    }
}
