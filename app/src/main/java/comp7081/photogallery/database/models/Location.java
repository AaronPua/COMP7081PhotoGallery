package comp7081.photogallery.database.models;

public class Location {

    int id;
    String latitude;
    String longitude;
    String address;
    String city;
    String state;
    String country;
    String postalCode;
    String knownName;
    String created_at;

    public Location(String address) {
        this.address = address;
    }

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String city, String state, String country, String postalCode) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Location(String address, String city, String state, String country, String postalCode) {
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

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // Getters
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

    public String getKnownName() {
        return this.knownName;
    }

    public String getCreated_at() {
        return this.created_at;
    }
}
