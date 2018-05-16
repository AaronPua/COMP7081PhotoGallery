package comp7081.photogallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.Photo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void filterPhotosByDate() {
        onView(withId(R.id.filterButton)).perform(click());

        onView(withId(R.id.startDate))
                .perform(typeText("2018-05-13"), closeSoftKeyboard());

        onView(withId(R.id.endDate))
                .perform(typeText("2018-05-13"), closeSoftKeyboard());

        onView(withId(R.id.filterSubmitButton)).perform(click());

        for (int i = 0; i <= 2; i++) {
            onView(withId(R.id.previousPhotoButton)).perform(click());
        }

        for (int i = 0; i <= 2; i++) {
            onView(withId(R.id.nextPhotoButton)).perform(click());
        }
    }

    @Test
    public void filterPhotosByCaption() {
        onView(withId(R.id.filterButton)).perform(click());

        onView(withId(R.id.caption))
                .perform(typeText("lighthouse"), closeSoftKeyboard());

        onView(withId(R.id.filterSubmitButton)).perform(click());

        for (int i = 0; i <= 2; i++) {
            onView(withId(R.id.previousPhotoButton)).perform(click());
        }

        for (int i = 0; i <= 2; i++) {
            onView(withId(R.id.nextPhotoButton)).perform(click());
        }
    }

    @Test
    public void filterPhotosByLatLong() {
        onView(withId(R.id.filterButton)).perform(click());

        onView(withId(R.id.latitudeEditText))
                .perform(typeText("37.422"), closeSoftKeyboard());

        onView(withId(R.id.longitudeEditText))
                .perform(typeText("-122.084"), closeSoftKeyboard());

        onView(withId(R.id.filterSubmitButton)).perform(click());

        for (int i = 0; i <= 2; i++) {
            onView(withId(R.id.previousPhotoButton)).perform(click());
        }

        for (int i = 0; i <= 2; i++) {
            onView(withId(R.id.nextPhotoButton)).perform(click());
        }
    }
}
