package com.github.cta_elevator_alerts;

import android.util.Log;

import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.work.impl.utils.SynchronousExecutor;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;

import com.github.cta_elevator_alerts.activities.MainActivity;
import com.github.cta_elevator_alerts.model.APIWorker;
import com.github.cta_elevator_alerts.model.StationDao;
import com.github.cta_elevator_alerts.model.StationRepository;
import com.github.cta_elevator_alerts.model.StationRoomDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Instrumented tests for Worker.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InstrumentedTestWorker {
    private StationDao stationDao;
    private StationRoomDatabase db;
    private StationRepository repository;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void setup() {
        Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();

        WorkManagerTestInitHelper.initializeTestWorkManager(mActivityRule.getActivity(), config);
    }

    @Test
    public void testPeriodicWorkAPIWorker() throws Exception {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(APIWorker.class, 15, TimeUnit.MINUTES)
                .addTag("Test")
                .build();

        WorkManager workManager = WorkManager.getInstance(mActivityRule.getActivity());
        TestDriver testDriver = WorkManagerTestInitHelper.getTestDriver(mActivityRule.getActivity());

        workManager.enqueueUniquePeriodicWork("Test", ExistingPeriodicWorkPolicy.KEEP, request).getResult().get();

        //Test that worker works even when activity is destroyed
        mActivityRule.finishActivity();
        assertNull(mActivityRule.getActivity());

        testDriver.setPeriodDelayMet(request.getId());
        WorkInfo workInfo = workManager.getWorkInfoById(request.getId()).get();
        assertEquals(workInfo.getState(), WorkInfo.State.ENQUEUED);
    }
}