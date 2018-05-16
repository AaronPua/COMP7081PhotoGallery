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

import comp7081.photogallery.database.BitmapUtility;
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

    @Test
    public void testGetPhotosByDate() throws Exception {
        String startDate = "2018-05-13";
        String endDate = "2018-05-13";
        ArrayList<Photo> photoArrayList = dbHelper.getPhotosByDate(startDate, endDate);
        for (Photo photo : photoArrayList) {
            assertEquals(startDate, photo.getDate());
        }
    }

    @Test
    public void testGetPhotosByCaption() throws Exception {
        String caption = "zebra";
        ArrayList<Photo> photoArrayList = dbHelper.getPhotosByCaption(caption);
        for (Photo photo : photoArrayList) {
            assertEquals(caption, photo.getCaption());
        }
    }

    @Test
    public void testGetPhotosByLatLong() throws Exception {
        String latitude = "37.422";
        String longitude = "-122.084";
        ArrayList<Photo> photoArrayList = dbHelper.getPhotosByLatLong(latitude, longitude);
        for (Photo photo : photoArrayList) {
            assertEquals(latitude, photo.getLatitude());
            assertEquals(longitude, photo.getLongitude());
        }
    }
}
