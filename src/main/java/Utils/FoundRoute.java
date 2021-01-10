package Utils;

import TaxiRide.City;
import TaxiRide.Ride;

import java.util.ArrayList;

public class FoundRoute {
    private ArrayList<Long> driveIds;
    private ArrayList<Ride> rides;
    private ArrayList<String> cityNames;

    public FoundRoute(){
        this.driveIds = new ArrayList<>();
        this.rides = new ArrayList<>();
        this.cityNames = new ArrayList<>();
    }

    public FoundRoute(ArrayList<Long> driveIds, ArrayList<Ride> rides, ArrayList<String> cityNames) {
        this.driveIds = driveIds;
        this.rides = rides;
        this.cityNames = cityNames;
    }

    public ArrayList<String> getCityNames() {
        return cityNames;
    }

    public void setCityNames(ArrayList<String> cityNames) {
        this.cityNames = cityNames;
    }

    public ArrayList<Long> getDriveIds() {
        return driveIds;
    }

    public void setDriveIds(ArrayList<Long> driveIds) {
        this.driveIds = driveIds;
    }

    public ArrayList<Ride> getRides() {
        return rides;
    }

    public void setRides(ArrayList<Ride> rides) {
        this.rides = rides;
    }

    public FoundRoute getPrefix(Integer num){
        FoundRoute prefix = new FoundRoute();
        for (int i = 0 ; i < num; i++){
            prefix.getRides().add(this.rides.get(i));
            prefix.getCityNames().add(this.cityNames.get(i));
            prefix.getDriveIds().add(this.driveIds.get(i));
        }
        return prefix;
    }
}
