package Utils;

import RestService.repository.UserRepository;
import TaxiRide.City;
import TaxiRide.Ride;
import TaxiRide.User;
import protos.TaxiRideProto;

import java.time.LocalDate;
import java.util.ArrayList;

public class protoUtils {
    public static TaxiRideProto.City.Builder getProtoFromCity(City city){
        return TaxiRideProto.City
                .newBuilder()
                .setCityId(city.getCity_id())
                .setCityName(city.getCity_name())
                .setX(city.getX())
                .setY(city.getY());
    }


    public static TaxiRideProto.Date.Builder getProtoFromDate(LocalDate date){
        return TaxiRideProto.Date
                .newBuilder()
                .setDay(date.getDayOfMonth())
                .setMonth(date.getMonthValue())
                .setYear(date.getYear());
    }

    public static LocalDate getDateFromProto(TaxiRideProto.Date protoDate){
        return LocalDate.of(protoDate.getYear(), protoDate.getMonth(), protoDate.getDay());
    }

    public static TaxiRideProto.RideRequest.Builder getProtoFromRide(Ride ride){
        return TaxiRideProto.RideRequest
                .newBuilder()
                .setFirstName(ride.getFirst_name())
                .setLastName(ride.getLast_name())
                .setPhoneNumber(ride.getPhone_number())
                .setDate(getProtoFromDate(ride.getDate()))
                .setStartLocation(getProtoFromCity(ride.getStart_location()))
                .setEndLocation(getProtoFromCity(ride.getEnd_location()))
                .setVacancies(ride.getVacancies())
                .setPd(ride.getPd());
    }

    public static TaxiRideProto.RideRequest.Builder getProtoFromRideWithId(Ride ride){
        TaxiRideProto.RideRequest.Builder rideRequest = getProtoFromRide(ride);
        rideRequest.setId(ride.getId());
        return rideRequest;
    }


    public static TaxiRideProto.UserRepoRequest.Builder getProtoFromUser(User user){

        TaxiRideProto.UserRequest.Builder userRequest = TaxiRideProto.UserRequest
                .newBuilder()
                .setFirstName(user.getFirst_name())
                .setLastName(user.getLast_name())
                .setDate(getProtoFromDate(user.getDate()))
                .setId(user.getId());
        for (City city : user.getCities_in_path()){
            userRequest.addCityPath(getProtoFromCity(city).build());
        }
        return TaxiRideProto.UserRepoRequest
                .newBuilder()
                .setUser(userRequest)
                .setStatus(UserRepoInstance.UserStatus.PENDING.ordinal());
    }

    public static TaxiRideProto.UserRepoRequest.Builder getProtoFromUser(UserRepoInstance user){

        TaxiRideProto.UserRequest.Builder userRequest = TaxiRideProto.UserRequest
                .newBuilder()
                .setFirstName(user.getFirst_name())
                .setLastName(user.getLast_name())
                .setDate(getProtoFromDate(user.getDate()))
                .setId(user.getId());
        for (City city : user.getCities_in_path()){
            userRequest.addCityPath(getProtoFromCity(city).build());
        }
        return TaxiRideProto.UserRepoRequest
                .newBuilder()
                .setUser(userRequest)
                .setStatus(user.getStatus().ordinal());
    }

    public static TaxiRideProto.RideRepoRequest.Builder getProtoFromRideRepo(RideRepoInstance ride){
        return TaxiRideProto.RideRepoRequest
                .newBuilder()
                .setRide(getProtoFromRideWithId(ride))
                .putAllHashmap(ride.getTimestamps());
    }

    public static UserRepoInstance getUserInstanceFromProto(TaxiRideProto.UserRepoRequest userRepoRequest){
        TaxiRideProto.UserRequest userRequest = userRepoRequest.getUser();
        TaxiRideProto.City source = userRequest.getLocation();
        LocalDate local_date = protoUtils.getDateFromProto(userRequest.getDate());
        ArrayList<City> path = new ArrayList<>();
        for (TaxiRideProto.City city : userRequest.getCityPathList()) {
            path.add(new City(city));
        }
        UserRepoInstance newUserRepoInstance = new UserRepoInstance(new City(source),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                local_date,
                path,
                UserRepoInstance.UserStatus.values()[userRepoRequest.getStatus()]);

        return newUserRepoInstance;
    }

    public static UserRepository getUserInstanceFromProto(TaxiRideProto.UserSnapshot snapshot){
        UserRepository userRepo = new UserRepository();
        for (TaxiRideProto.UserRepoRequest userRepoRequest : snapshot.getUserSnapshotList()){
            UserRepoInstance currUser = getUserInstanceFromProto(userRepoRequest);
            userRepo.save(currUser);
        }
        userRepo.setIdIndex(snapshot.getIndex());
        return userRepo;
    }

    public static TaxiRideProto.UserSnapshot.Builder getProtoFromUserSnapshot(UserRepository userRepo){
        TaxiRideProto.UserSnapshot.Builder userSnapshotBuilder = TaxiRideProto.UserSnapshot.newBuilder();
        userSnapshotBuilder.setIndex(userRepo.getIdIndex());
        for (UserRepoInstance user : userRepo.findAll()){
            userSnapshotBuilder.addUserSnapshot(protoUtils.getProtoFromUser(user)).build();
        }
        return userSnapshotBuilder;
    }
}
