package comp7081.photogallery;

import org.junit.Before;
import org.junit.Test;

import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.Photo;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DatabaseUnitTest {

    DatabaseHelper dbHelper;

    @Test
    public void dataPhotoStorage() {
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