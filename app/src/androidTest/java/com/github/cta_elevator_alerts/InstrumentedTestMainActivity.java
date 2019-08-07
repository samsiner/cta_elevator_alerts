package com.github.cta_elevator_alerts;

import android.content.Context;
import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.cta_elevator_alerts.activities.AddFavoriteActivity;
import com.github.cta_elevator_alerts.activities.AllLinesActivity;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.MainActivity;
import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationDao;
import com.github.cta_elevator_alerts.model.StationRepository;
import com.github.cta_elevator_alerts.model.StationRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.getIntents;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented tests for MainActivity.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InstrumentedTestMainActivity {
    private StationDao stationDao;
    private StationRoomDatabase db;
    private StationRepository repository;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void createDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, StationRoomDatabase.class).build();
        stationDao = db.getDao();
        repository = StationRepository.getInstance(mActivityRule.getActivity().getApplication());

        //Create and add sample station
        Station station = new Station("40900");
        stationDao.insert(station);
        stationDao.updateName("40900", "Howard");
        stationDao.setRedTrue("40900");
        stationDao.setPurpleTrue("40900");
        stationDao.setYellowTrue("40900");
        stationDao.addFavorite("40900", "Home");
        stationDao.setHasElevator("40900");
        stationDao.setAlert("40900", "short description", "begin date time");
    }

    @Test
    public void testAddFavoriteButton(){
        onView(withId(R.id.button_addFavorite)).perform(click());
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(AddFavoriteActivity.class);
    }

    @Test
    public void testAllLinesButton(){
        onView(withId(R.id.btn_to_all_lines)).perform(click());
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(AllLinesActivity.class);
    }

    @Test
    public void testDisplayAlertDetailsFromMainActivityAlert(){
        onView(withId(R.id.recycler_station_alerts))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(DisplayAlertActivity.class);
    }

    //Must add at least one favorite first
    @Test
    public void testDisplayAlertDetailsFromMainActivityFavorites(){
        onView(withId(R.id.recycler_favorite_stations))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(DisplayAlertActivity.class);
    }

    @Test
    public void checkDatabaseAndDao() {
        assertEquals(stationDao.getName("40900"), "Howard");
        assertTrue(stationDao.getHasElevator("40900"));
        assertTrue(stationDao.getRed("40900"));
        assertTrue(stationDao.getPurple("40900"));
        assertTrue(stationDao.getYellow("40900"));
    }

    @Test
    public void checkRepository() {
        assertTrue(repository.mGetAllRoutes("40900")[0]);
        assertEquals(repository.mGetStationName("40900"), "Howard");
        assertTrue(repository.mGetHasElevator("40900"));
    }

    @After
    public void closeDb() {
        db.close();
    }
}