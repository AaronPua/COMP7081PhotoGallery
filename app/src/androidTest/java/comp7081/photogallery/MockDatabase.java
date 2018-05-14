package comp7081.photogallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.Photo;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MockDatabase {

    @Mock
    Context mockContext;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context ctx;

    @Before
    public void createDB() {
        mockContext = InstrumentationRegistry.getTargetContext();
        ctx = new MockContext();
        dbHelper = new DatabaseHelper(mockContext);
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
}
