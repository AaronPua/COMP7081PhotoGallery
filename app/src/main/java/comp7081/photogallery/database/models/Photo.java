package comp7081.photogallery.database.models;

public class Photo {
    int id;
    byte[] image;
    String name;
    String date;
    String caption;

    public Photo(byte[] image, String name, String date) {
        this.image = image;
        this.name = name;
        this.date = date;
    }

    public Photo(byte[] image, String name, String date, String caption) {
        this.image = image;

        this.name = name;
        this.date = date;
        this.caption = caption;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public byte[] getImage() {
        return this.image;
    }

    public String getName() {
        return this.name;
    }

    public String getDate() {
        return this.date;
    }

    public String getCaption() {
        return this.caption;
    }

}
