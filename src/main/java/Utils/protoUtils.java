package Utils;

import TaxiRide.City;
import TaxiRide.Ride;
import TaxiRide.User;
import protos.TaxiRideProto;

import java.io.FileDescriptor;
import java.time.LocalDate;
import java.util.HashMap;

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
}
