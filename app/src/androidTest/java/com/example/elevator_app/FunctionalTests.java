package com.example.elevator_app;

import android.app.Instrumentation;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.TextView;

import com.example.elevator_app.Activities.AddFavoriteActivity;
import com.example.elevator_app.Activities.DisplayAlertActivity;
import com.example.elevator_app.Activities.MainActivity;

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
public class FunctionalTests {

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
            assertNotNull("toolbar exists", getMainActivity().findViewById(R.id.toolbar_old));
            assertNotNull("stations out exists", getMainActivity().findViewById(R.id.text_elevDownTempList));
            assertNotNull("favorites exists", getMainActivity().findViewById(R.id.text_favoritesList));
        });
    }

    /**
     * Verifies transition from MainActivity to AddFavoriteActivity
     */
    @Test
    public void testTransitionToAddFavorite(){
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AddFavoriteActivity.class.getName(), null, false);

        getMainActivity().runOnUiThread(() -> {
            Button button = getMainActivity().findViewById(R.id.button_addFavorite);
            button.performClick();
        });

        AddFavoriteActivity activity = (AddFavoriteActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull("activity transition success", activity);
        activity.finish();
    }

    /**
     * Verifies transition from MainActivity to DisplayAlertActivity
     */
    @Test
    public void testTransitionToDisplayAlert(){
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(DisplayAlertActivity.class.getName(), null, false);

        getMainActivity().runOnUiThread(() -> {
            TextView textView1 = getMainActivity().findViewById(40780);
            textView1.performClick();
        });

        DisplayAlertActivity activity = (DisplayAlertActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull("activity transition success", activity);
        activity.finish();
    }
}
