package com.example.elevator_app;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.example.elevator_app", appContext.getPackageName());
//    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule(MainActivity.class);
    public ActivityTestRule<AddFavoriteActivity> addFavoriteActivity = new ActivityTestRule(AddFavoriteActivity.class);

    protected MainActivity getMainActivity() { return mainActivity.getActivity(); }
    protected AddFavoriteActivity getAddFavoriteActivity() { return addFavoriteActivity.getActivity(); }

    /**
     * Verifies that the activity under test can be launched.
     */
    @Test
    public void testMainActivitySetup(){
        assertNotNull("activity is set up properly", getMainActivity());

        getMainActivity().runOnUiThread(() -> {
            assertNotNull("button exists", getMainActivity().findViewById(R.id.button_addFavorite));
            assertNotNull("toolbar exists", getMainActivity().findViewById(R.id.toolbar));
            assertNotNull("stations out exists", getMainActivity().findViewById(R.id.text_elevDownTempList));
            assertNotNull("favorites exists", getMainActivity().findViewById(R.id.text_favoritesList));
        });
    }

    @Test
    public void testTransitionToAddFavorite(){
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AddFavoriteActivity.class.getName(), null, false);

        getMainActivity().runOnUiThread(() -> {
            Button button = (Button) getMainActivity().findViewById(R.id.button_addFavorite);
            button.performClick();
        });

        AddFavoriteActivity activity = (AddFavoriteActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull("activity transition success", activity);
        activity.finish();
    }

}
