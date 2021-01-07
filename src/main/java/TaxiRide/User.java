package TaxiRide;

import protos.TaxiRideProto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Vector;


public class User {
    private City location;
    private String first_name;
    private String last_name;
    private LocalDate date;
    private ArrayList<City> cities_in_path;
    private Long id;


    public User(City location, String first_name, String last_name, LocalDate date, ArrayList<City> cities_in_path) {
        this.location = location;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date = date;
        this.cities_in_path = cities_in_path;
    }

    public City getLocation() {
        return location;
    }

    public void setLocation(City location) {
        this.location = location;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<City> getCities_in_path() {
        return cities_in_path;
    }

    public void setCities_in_path(ArrayList<City> cities_in_path) {
        this.cities_in_path = cities_in_path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(location, user.location) &&
                Objects.equals(first_name, user.first_name) &&
                Objects.equals(last_name, user.last_name) &&
                Objects.equals(date, user.date) &&
                Objects.equals(cities_in_path, user.cities_in_path) &&
                id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, first_name, last_name, date, cities_in_path, id);
    }

    @Override
    public String toString() {
        return "User{" +
                "location=" + location +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", date=" + date +
                ", cities_in_path=" + cities_in_path +
                ", id=" + id +
                '}';
    }
}
