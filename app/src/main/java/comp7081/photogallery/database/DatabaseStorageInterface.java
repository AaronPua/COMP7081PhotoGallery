package comp7081.photogallery.database;

import android.graphics.Bitmap;

import java.util.ArrayList;

import comp7081.photogallery.database.models.LocationInfo;
import comp7081.photogallery.database.models.Photo;

public interface DatabaseStorageInterface {
    void addPhotoEntry(Photo photo);
    ArrayList<Bitmap> getAllPhotos();
    ArrayList<Bitmap> getPhotosByDate(String startDate, String endDate);
    ArrayList<Bitmap> getPhotosByCaption(String caption);
    void updateCaption(int id, String caption);
    void addLocationForPhoto(Photo photo, LocationInfo locationInfo);
}
