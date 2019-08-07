package com.github.cta_elevator_alerts;

import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.SpecificLineActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.getIntents;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;

/**
 * Instrumented tests for SpecificLineActivity.
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTestSpecificLineActivity {
    @Rule
    public IntentsTestRule<SpecificLineActivity> mActivityRule = new IntentsTestRule<SpecificLineActivity>(
            SpecificLineActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("line", "Red Line");
            intent.putExtra("fromFavorites", false);
            intent.putExtra("nickname", "Unknown");
            return intent;
        }
    };

    @Test
    public void testToDisplayAlertFromSpecificLineActivity(){
        onView(withId(R.id.recycler_specific_line))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(DisplayAlertActivity.class);
    }
}