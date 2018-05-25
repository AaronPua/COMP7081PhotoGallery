package comp7081.photogallery.database.models;

import android.graphics.Bitmap;

public class Photo {
    int id;
    String image;
    String name;
    String date;
    String caption;
    Bitmap bitmap;
    String latitude;
    String longitude;

    public Photo() {

    }

    public Photo(String image, String name, String date) {
        this.image = image;
        this.name = name;
        this.date = date;
    }

    public Photo(String image, String name, String date, String caption) {
        this.image = image;
        this.name = name;
        this.date = date;
        this.caption = caption;
    }

    public Photo(Bitmap bitmap, String name, String date, String caption) {
        this.bitmap = bitmap;
        this.name = name;
        this.date = date;
        this.caption = caption;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getImage() {
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

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
}
