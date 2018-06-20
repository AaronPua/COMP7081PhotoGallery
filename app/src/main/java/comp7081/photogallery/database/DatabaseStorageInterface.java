package comp7081.photogallery.database;

import java.util.ArrayList;

import comp7081.photogallery.database.models.LocationInfo;
import comp7081.photogallery.database.models.Photo;

public interface DatabaseStorageInterface {
    void addPhotoEntry(Photo photo);
    ArrayList<Photo> getAllPhotos();
    ArrayList<Photo> getPhotosByDate(String startDate, String endDate);
    ArrayList<Photo> getPhotosByCaption(String caption);
    void updateCaption(int id, String caption);
    void addLocationForPhoto(Photo photo, LocationInfo locationInfo);
}
