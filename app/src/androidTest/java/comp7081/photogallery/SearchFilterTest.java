package comp7081.photogallery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import comp7081.photogallery.Helpers.BitmapUtility;
import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.LocationInfo;
import comp7081.photogallery.database.models.Photo;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchFilterTest {

    Context context;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String latitude;
    String longitude;
    String address;
    String city;
    String state;
    String country;
    String postalCode;
    Location location;
    double currentLat;
    double currentLong;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    private void createPhoto() throws Exception {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_test_2);
        byte[] byteArray = BitmapUtility.convertBitmapToByteArray(bitmap);
        Random rand = new Random();
        int randInt = rand.nextInt(100000000);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String randomName = "JPG_" + date + "_" + String.valueOf(randInt);

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {}
        if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLat = location.getLatitude();
            currentLong = location.getLongitude();
            latitude = location.convert(currentLat, location.FORMAT_DEGREES);
            longitude = location.convert(currentLong, location.FORMAT_DEGREES);
        } else if (lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            currentLat = location.getLatitude();
            currentLong = location.getLongitude();
            latitude = location.convert(currentLat, location.FORMAT_DEGREES);
            longitude = location.convert(currentLong, location.FORMAT_DEGREES);
        } else {
            currentLat = 37.422;
            currentLong = -122.084;
            latitude = location.convert(currentLat, location.FORMAT_DEGREES);
            longitude = location.convert(currentLong, location.FORMAT_DEGREES);
        }

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(currentLat, currentLong, 1);

            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
        } catch (IOException e) {
            Log.e("TAG", "getFromLocation: got IOException", e);
            address = "1599 Amphitheatre Pkwy, Mountain View, CA 94043, USA";
            city = "Mountain View";
            state = "California";
            country = "United States";
            postalCode = "94043";
        }

        Photo photo = new Photo(byteArray, randomName, date);
        dbHelper.addPhotoEntry(photo);

        LocationInfo locationInfo = new LocationInfo(latitude, longitude, address, city, state, country, postalCode);
        dbHelper.addLocationForPhoto(photo, locationInfo);
    }

    @Test
    public void testGetPhotosByDate() throws Exception {
        String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        db.beginTransaction();
        try {
            createPhoto();
            ArrayList<Photo> photoArrayList = dbHelper.getPhotosByDate(startDate, endDate);
            if(photoArrayList != null) {
                for (Photo photo : photoArrayList) {
                    assertEquals(startDate, photo.getDate());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
    }

    @Test
    public void testGetPhotosByCaption() throws Exception {
        String caption = "zebra";
        db.beginTransaction();
        try {
            ArrayList<Photo> photoArrayList = dbHelper.getPhotosByCaption(caption);
            if(photoArrayList != null) {
                for (Photo photo : photoArrayList) {
                    assertEquals(caption, photo.getCaption());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();
    }

    @Test
    public void testGetPhotosByLatLong() throws Exception {
        String latitude = "37.422";
        String longitude = "-122.084";
        db.beginTransaction();
        try {
            createPhoto();
            ArrayList<Photo> photoArrayList = dbHelper.getPhotosByLatLong(latitude, longitude);
            if(photoArrayList != null) {
                for (Photo photo : photoArrayList) {
                    assertEquals(latitude, photo.getLatitude());
                    assertEquals(longitude, photo.getLongitude());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.endTransaction();
        db.close();

    }
}
