package TaxiRide;


public class City {
    public String city_name;
    public Integer city_id;
    public Integer[] city_center;

    public City(String city_name, Integer city_id, Integer[] city_center) {
        if (city_center.length != 2){
            throw new RuntimeException("Bad City location");
        }
        this.city_name = city_name;
        this.city_id = city_id;
        this.city_center = city_center;
    }

    public String getCity_name() {
        return city_name;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public Integer[] get_location() {
        return city_center;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void set_location(Integer city_center_x, Integer city_center_y) {
        this.city_center[0] = city_center_x;
        this.city_center[1] = city_center_y;
    }


}
