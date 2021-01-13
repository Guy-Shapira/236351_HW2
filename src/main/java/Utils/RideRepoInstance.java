package Utils;

import TaxiRide.City;
import TaxiRide.Ride;
import com.google.common.collect.Multimap;
import protos.TaxiRideProto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RideRepoInstance extends Ride {
    private final static long THRESHOLD = 5000;
    private Map<String, Long> timestamps;
    public RideRepoInstance(String first_name, String last_name, Integer phone_number, City start_location, City end_location, LocalDate date, Integer vacancies, Integer pd) {
        super(first_name, last_name, phone_number, start_location, end_location, date, vacancies, pd);
        this.timestamps = new HashMap<>();
    }

    public RideRepoInstance(Ride newRide) {
        super(newRide.getFirst_name(), newRide.getLast_name(), newRide.getPhone_number(), newRide.getStart_location(),
                newRide.getEnd_location(), newRide.getDate(), newRide.getVacancies(), newRide.getPd());
        this.timestamps = new HashMap<>();
    }


    public Map<String, Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Map<String, Long> timestamps) {
        this.timestamps = timestamps;
    }

    public void deleteTimeStamp(String key){
        if (timestamps.containsKey(key)){
            timestamps.remove(key);
        }
    }

    public boolean insertTimeStamp(String key, Long value){
        if (!timestamps.containsKey(key)){
            timestamps.put(key, value);
            return true;
        }
        return false;
    }

    public void upVacancies(String key) {
        if (!this.timestamps.containsKey(key)) {
            this.deleteTimeStamp(key);
        } else {
            this.vacancies += 1;
        }
    }

    public void lowerVacancies(String serverName, long driveId) throws Errors.FullRide, Errors.AlreadyReservedTheRide {
        if (this.vacancies <= 0){
            throw new Errors.FullRide();
        }else{
            String keyName = serverName + ":" + driveId;
            if (this.insertTimeStamp(keyName, System.currentTimeMillis())){
                this.vacancies -= 1;
            }else{
                throw new Errors.AlreadyReservedTheRide();
            }
        }
    }

    public void cleanupReservations(long currentTimestamp){
        for (String key : this.timestamps.keySet()){
            long value = timestamps.get(key);
            if (currentTimestamp - value > THRESHOLD){
                this.upVacancies(key);
            }
        }
    }

}
