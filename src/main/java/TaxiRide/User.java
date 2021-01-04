package TaxiRide;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public class User {
    private City location;
    private String first_name;
    private String last_name;
    private Long id;


    public User(City location, String first_name, String last_name) {
        this.location = location;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public City getLocation() {
        return location;
    }

    public void setLocation(City location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return location.equals(user.location) &&
                first_name.equals(user.first_name) &&
                last_name.equals(user.last_name) &&
                Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, first_name, last_name, id);
    }

    @Override
    public String toString() {
        return "User{" +
                "location=" + location +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", id=" + id +
                '}';
    }

    public void path_planning(List<City> cities, List<LocalDate> dates){
        // TODO: here is the hw
        return;
    }
}
