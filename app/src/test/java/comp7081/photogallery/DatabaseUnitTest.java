package comp7081.photogallery;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.Photo;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DatabaseUnitTest {

    DatabaseHelper dbHelper;
    Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @After
    public void tearDown() {

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = RuntimeEnvironment.application;
        assertEquals("comp7081.photogallery", appContext.getPackageName());
    }

    @Test
    public void dataPhotoStorage() {
        context = RuntimeEnvironment.application;
        Photo photo = new Photo();
        photo.setDate("2018-05-08");
        assertEquals("2018-05-08", photo.getDate());
    }

    @Test
    public void dataLocationStorage() {
        Photo photo = new Photo();
        photo.setDate("2018-05-08");
        assertEquals("2018-05-08", photo.getDate());
    }
}