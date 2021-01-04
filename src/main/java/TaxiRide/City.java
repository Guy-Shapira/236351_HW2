package TaxiRide;


public class City {
    private String city_name;
    private Integer city_id;
    private Integer x;
    private Integer y;

    public City(String city_name, Integer city_id, Integer x, Integer y) {
        this.city_name = city_name;
        this.city_id = city_id;
        this.x = x;
        this.y = y;
    }

    public String getCity_name() {
        return city_name;
    }

    public Integer getCity_id() {
        return city_id;
    }


    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
