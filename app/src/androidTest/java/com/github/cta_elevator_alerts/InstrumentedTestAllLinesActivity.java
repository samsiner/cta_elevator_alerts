package com.github.cta_elevator_alerts;

import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.cta_elevator_alerts.activities.AllLinesActivity;
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
 * Instrumented tests for AllLinesActivity.
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTestAllLinesActivity {
    @Rule
    public IntentsTestRule<AllLinesActivity> mActivityRule = new IntentsTestRule<>(
            AllLinesActivity.class);

    @Test
    public void testToSpecificLineFromAllLines(){
        onView(withId(R.id.recycler_all_lines))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(SpecificLineActivity.class);
    }
}