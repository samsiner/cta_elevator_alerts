package com.github.cta_elevator_alerts;

import android.content.Intent;

import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.cta_elevator_alerts.activities.AddFavoriteActivity;
import com.github.cta_elevator_alerts.activities.AllLinesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.getIntents;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;

/**
 * Instrumented tests for AddFavoriteActivity.
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTestAddFavoriteActivity {
    @Rule
    public IntentsTestRule<AddFavoriteActivity> mActivityRule = new IntentsTestRule<>(
            AddFavoriteActivity.class);

    @Test
    public void testToAllLinesFromAddFavorite(){
        onView(withId(R.id.relative_station)).perform(click());
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(AllLinesActivity.class);
    }
}