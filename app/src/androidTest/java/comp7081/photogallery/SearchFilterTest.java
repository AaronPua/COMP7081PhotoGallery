package comp7081.photogallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import comp7081.photogallery.database.BitmapUtility;
import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.Photo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchFilterTest {

    Context appContext;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context ctx;

    @Before
    public void createDB() {
        appContext = InstrumentationRegistry.getTargetContext();
        ctx = new MockContext();
        dbHelper = new DatabaseHelper(appContext);
        db = dbHelper.getReadableDatabase();
    }

    @After
    public void closeDB() throws IOException {
        db.close();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("comp7081.photogallery", appContext.getPackageName());
    }

    /*@Test
    public void testSaveGetDeletePhoto() throws Exception {
        Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_action_test);
        byte[] image = BitmapUtility.convertBitmapToByteArray(bitmap);
        Random rand = new Random();
        int randInt = rand.nextInt(100000000);
        String randomName = "JPG_2018_05_09_" + String.valueOf(randInt);
        Photo photo = new Photo();
        photo.setImage(image);
        photo.setName(randomName);
        photo.setDate("2018-05-09");
        photo.setCaption("random");
        dbHelper.addPhotoEntry(photo);
        ArrayList<Photo> photoArrayList = dbHelper.getAllPhotos();
        int lastIndex = photoArrayList.size() - 1;
        assertEquals(randomName, photoArrayList.get(lastIndex).getName());
        assertEquals("2018-05-09", photoArrayList.get(lastIndex).getDate());
        assertEquals("random", photoArrayList.get(lastIndex).getCaption());
        dbHelper.deletePhotoEntry(photo);
    }*/

    @Test
    public void testGetPhotosByDate() {
        String startDate = "2018-05-11";
        String endDate = "2018-05-12";
        int i;
        ArrayList<Photo> photoArrayList = dbHelper.getPhotosByDate(startDate, endDate);
        for (i = 0; i < photoArrayList.size(); i++) {
            assertEquals("2018-05-11", photoArrayList.get(i).getDate());
        }
    }

}
