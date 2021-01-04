package TaxiRide;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public class User {
    private City location;

    public User(City location) {
        this.location = location;
    }

    public City getLocation() {
        return location;
    }

    public void setLocation(City location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return location.equals(user.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return "User{" +
                "location=" + location +
                '}';
    }

    public void path_planning(List<City> cities, List<LocalDate> dates){
        // TODO: here is the hw
        return;
    }
}
