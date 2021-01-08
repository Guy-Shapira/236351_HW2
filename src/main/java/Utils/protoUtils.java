package Utils;

import TaxiRide.City;
import protos.TaxiRideProto;

import java.time.LocalDate;

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
}
