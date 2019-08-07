package com.github.cta_elevator_alerts;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented tests for DisplayAlertActivity.
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTestDisplayAlertActivity {
    @Rule
    public IntentsTestRule<DisplayAlertActivity> mActivityRule = new IntentsTestRule<DisplayAlertActivity>(
            DisplayAlertActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("stationID", "40900");
            return intent;
        }
    };

    @Test
    public void test(){
        onView(withId(R.id.txt_toolbar)).check(matches(withText("Howard")));
    }
}