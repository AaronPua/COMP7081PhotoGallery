package comp7081.photogallery.database;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface DatabaseStorageInterface {
    void addPhotoEntry(byte[] image, String name, String date, String caption, String location);
    ArrayList<Bitmap> getAllPhotos();
    ArrayList<Bitmap> getPhotosByDate(String startDate, String endDate);
    ArrayList<Bitmap> getPhotosByCaption(String caption);
    void updateCaption(int id, String caption);
}
