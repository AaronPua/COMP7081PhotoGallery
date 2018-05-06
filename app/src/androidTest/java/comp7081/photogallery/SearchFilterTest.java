package comp7081.photogallery;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchFilterTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("comp7081.photogallery", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void filterPhotosByDate() {
        onView(withId(R.id.filterButton)).perform(click());

        onView(withId(R.id.startDate))
                .perform(typeText("2018-05-01"), closeSoftKeyboard());

        onView(withId(R.id.endDate))
                .perform(typeText("2018-05-01"), closeSoftKeyboard());

        onView(withId(R.id.filterSubmitButton)).perform(click());
    }

    @Test
    public void filterPhotosByCaption() {
        onView(withId(R.id.filterButton)).perform(click());

        onView(withId(R.id.caption))
                .perform(typeText("zebra"), closeSoftKeyboard());

        onView(withId(R.id.filterSubmitButton)).perform(click());
    }
}
