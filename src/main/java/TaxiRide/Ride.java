package TaxiRide;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;

public class Ride {
    public String first_name;
    public String last_name;
    public Integer phone_number;
    public City start_location;
    public City end_location;
    public LocalDate date;
    public Integer vacancies;
    public Integer pd;

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
}