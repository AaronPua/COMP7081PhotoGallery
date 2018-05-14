package comp7081.photogallery.database.models;

public class LocationInfo {

    int id;
    int photo_id;
    String latitude;
    String longitude;
    String address;
    String city;
    String state;
    String country;
    String postalCode;

    // Constructors
    public LocationInfo() {

    }

    public LocationInfo(String latitude, String longitude, String address, String city, String state, String country, String postalCode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    // Getters
    public int getPhoto_id() {
        return this.photo_id;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public int getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPostalCode() {
        return this.postalCode;
    }
}
