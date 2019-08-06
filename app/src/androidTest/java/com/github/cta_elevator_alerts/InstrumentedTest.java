package com.github.cta_elevator_alerts;

import android.content.Context;
import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.getIntents;

import static androidx.test.ext.truth.content.IntentSubject.assertThat;

import com.github.cta_elevator_alerts.activities.AddFavoriteActivity;
import com.github.cta_elevator_alerts.activities.AllLinesActivity;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for temp.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InstrumentedTest {
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
        stationDao.nukeTable();
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
    public void startActivityClickAddFavoriteButton(){
        onView(withId(R.id.button_addFavorite)).perform(click());
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(AddFavoriteActivity.class);
    }

    @Test
    public void startActivityClickAllLines(){
        onView(withId(R.id.btn_to_all_lines)).perform(scrollTo(), click());
        Intent receivedIntent = Iterables.getOnlyElement(getIntents());
        assertThat(receivedIntent).hasComponentClass(AllLinesActivity.class);
    }

    @Test
    public void checkDatabaseAndDao() {
        assertEquals(stationDao.getFavoritesCount(), 1);
        assertEquals(stationDao.getName("40900"), "Howard");
        assertEquals(stationDao.getShortDescription("40900"), "short description");
        assertEquals(stationDao.getBeginDateTime("40900"), "begin date time");
        assertTrue(stationDao.getHasElevator("40900"));
        assertTrue(stationDao.getHasElevatorAlert("40900"));
        assertTrue(stationDao.isFavoriteStation("40900"));
        assertTrue(stationDao.getRed("40900"));
        assertTrue(stationDao.getPurple("40900"));
        assertTrue(stationDao.getYellow("40900"));
    }

    @Test
    public void checkRepository() {
        assertEquals(repository.getFavoritesCount(), 1);
        assertEquals(repository.getAlertsCount(), 1);
        assertEquals(repository.mGetAllRoutes("40900"), new boolean[]{true, false, false, false, false, false, true, true});
        assertEquals(repository.mGetStationName("40900"), "Howard");
        assertTrue(repository.mGetHasElevator("40900"));
        assertTrue(repository.mGetHasElevatorAlert("40900"));
        assertEquals(repository.getAlertDetails("40900").get(0), "Howard");

        Station station2 = new Station("40901");
        stationDao.insert(station2);

        stationDao.setAlert("40901", "alert", "date time");
        assertEquals(repository.getAlertsCount(), 2);

        repository.addFavorite("40901", "Nickname");
        assertEquals(repository.getFavoritesCount(), 2);

        repository.removeFavorite("40901");
        assertEquals(repository.getFavoritesCount(), 1);
    }

    @After
    public void closeDb() {
        db.close();
    }
}