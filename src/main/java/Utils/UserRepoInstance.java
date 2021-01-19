package Utils;

import TaxiRide.City;
import TaxiRide.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class UserRepoInstance extends User {
    public enum UserStatus {
        PENDING,
        COMPLETE
    }
    private UserStatus userStatus;
    public UserRepoInstance(City location, String first_name, String last_name, LocalDate date, ArrayList<City> cities_in_path) {
        super(location, first_name, last_name, date, cities_in_path);
        this.userStatus = UserStatus.PENDING;
    }
    public UserRepoInstance(City location, String first_name, String last_name, LocalDate date, ArrayList<City> cities_in_path, UserStatus userStatus) {
        super(location, first_name, last_name, date, cities_in_path);
        this.userStatus = userStatus;
    }
    public UserRepoInstance(City location, String first_name, String last_name, LocalDate date, ArrayList<City> cities_in_path, UserStatus userStatus, long id) {
        super(location, first_name, last_name, date, cities_in_path);
        this.userStatus = userStatus;
        this.setId(id);
    }

    public UserRepoInstance(User user){
        super(user.getLocation(), user.getFirst_name(), user.getLast_name(), user.getDate(), user.getCities_in_path());
        this.userStatus = UserStatus.PENDING;
    }


    public UserStatus getStatus() {
        return userStatus;
    }
    public void setStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRepoInstance that = (UserRepoInstance) o;
        return userStatus == that.userStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userStatus);
    }


}
