package TaxiRide;

import java.time.LocalDate;
import java.util.Objects;
import Utils.Errors;
import protos.TaxiRideProto;
import Utils.protoUtils;

public class Ride {
    private String first_name;
    private String last_name;
    private Integer phone_number;
    private City start_location;
    private City end_location;
    private LocalDate date;
    protected Integer vacancies;
    private Integer pd;
    private Long id;


    public Ride(){
        super();
    }

    public Ride(String first_name, String last_name, Integer phone_number, City start_location, City end_location,
                LocalDate date, Integer vacancies, Integer pd) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.start_location = start_location;
        this.end_location = end_location;
        this.date = date;
        this.vacancies = vacancies;
        this.pd = pd;
    }

    public Ride(TaxiRideProto.RideRequest driveLeg) {
        this.first_name = driveLeg.getFirstName();
        this.last_name = driveLeg.getLastName();
        this.phone_number = driveLeg.getPhoneNumber();
        this.start_location = new City(driveLeg.getStartLocation());
        this.end_location = new City(driveLeg.getEndLocation());
        this.date = protoUtils.getDateFromProto(driveLeg.getDate());
        this.vacancies = driveLeg.getVacancies();
        this.pd = driveLeg.getPd();
    }

    public Ride(TaxiRideProto.RideRequest driveLeg, long id) {
        this.first_name = driveLeg.getFirstName();
        this.last_name = driveLeg.getLastName();
        this.phone_number = driveLeg.getPhoneNumber();
        this.start_location = new City(driveLeg.getStartLocation());
        this.end_location = new City(driveLeg.getEndLocation());
        this.date = protoUtils.getDateFromProto(driveLeg.getDate());
        this.vacancies = driveLeg.getVacancies();
        this.pd = driveLeg.getPd();
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

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public City getStart_location() {
        return start_location;
    }

    public void setStart_location(City start_location) {
        this.start_location = start_location;
    }

    public City getEnd_location() {
        return end_location;
    }

    public void setEnd_location(City end_location) {
        this.end_location = end_location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public Integer getPd() {
        return pd;
    }

    public void setPd(Integer pd) {
        this.pd = pd;
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
        Ride ride = (Ride) o;
        return first_name.equals(ride.first_name) &&
                last_name.equals(ride.last_name) &&
                phone_number.equals(ride.phone_number) &&
                start_location.equals(ride.start_location) &&
                end_location.equals(ride.end_location) &&
                date.equals(ride.date) &&
                vacancies.equals(ride.vacancies) &&
                pd.equals(ride.pd) &&
                Objects.equals(id, ride.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, last_name, phone_number, start_location, end_location, date, vacancies, pd, id);
    }

    @Override
    public String toString() {
        return "Ride{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone_number=" + phone_number +
                ", start_location=" + start_location +
                ", end_location=" + end_location +
                ", date=" + date +
                ", vacancies=" + vacancies +
                ", pd=" + pd +
                ", id=" + id +
                '}';
    }

    public boolean in_deviation_distance(City another_city){
        Integer lhs = (this.end_location.getX() - this.start_location.getX()) *
                (this.start_location.getY() - another_city.getY());
        Integer rhs = (this.start_location.getX() - another_city.getX())
                * (this.end_location.getY() - start_location.getY());
        double bottom_part = Math.sqrt(Math.pow(this.end_location.getX() - this.start_location.getX(), 2) +
                        Math.pow(this.end_location.getY() - this.start_location.getY(), 2));
        double total_dist = Math.abs(lhs - rhs) / bottom_part;
        return total_dist <= this.getPd();
    }
}