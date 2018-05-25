package comp7081.photogallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Random;

import comp7081.photogallery.Helpers.BitmapUtility;
import comp7081.photogallery.database.DatabaseHelper;
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
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_test);
        byte[] image = BitmapUtility.convertBitmapToByteArray(bitmap);
        Random rand = new Random();
        int randInt = rand.nextInt(100000000);
        String randomName = "JPG_2018_05_13_" + String.valueOf(randInt);
        Photo photo = new Photo();
        //photo.setImage(image);
        photo.setName(randomName);
        photo.setDate("2018-05-13");
        photo.setCaption("zebra");
        dbHelper.addPhotoEntry(photo);
    }

    @Test
    public void testGetPhotosByDate() throws Exception {
        String startDate = "2018-05-13";
        String endDate = "2018-05-13";
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
