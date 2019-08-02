package com.github.cta_elevator_alerts;

import android.content.Intent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.cta_elevator_alerts.activities.AddFavoriteActivity;
import com.github.cta_elevator_alerts.activities.AllLinesActivity;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.MainActivity;
import com.github.cta_elevator_alerts.activities.SpecificLineActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class FunctionalTest {
    private MainActivity mainActivity;
    private AddFavoriteActivity addFavoriteActivity;
    private AllLinesActivity allLinesActivity;
    private SpecificLineActivity specificlineActivity;

   @Before
   public void setup(){
       mainActivity = Robolectric.buildActivity(MainActivity.class).create().start().visible().get();
       addFavoriteActivity = Robolectric.buildActivity(AddFavoriteActivity.class).create().start().visible().get();
       allLinesActivity = Robolectric.buildActivity(AllLinesActivity.class).create().start().visible().get();
   }

    protected void runUIThreadTasks(){
       org.robolectric.shadows.ShadowLooper.runUiThreadTasks();
   }

   @Test
    public void testActivitySetup(){
       assertNotNull(mainActivity);
   }

   @Test
    public void testAllLinesButton(){
       mainActivity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               RelativeLayout b = mainActivity.findViewById(R.id.btn_to_all_lines);
               b.performClick();

               Intent expectedIntent = new Intent(mainActivity, AllLinesActivity.class);
               ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
               Intent actual = shadowActivity.getNextStartedActivity();
               assertEquals(expectedIntent.getComponent(), actual.getComponent());
           }
       });
   }

    @Test
    public void testAddFavoriteButton(){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button b = mainActivity.findViewById(R.id.button_addFavorite);
                b.performClick();

                Intent expectedIntent = new Intent(mainActivity, AddFavoriteActivity.class);
                ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
                Intent actual = shadowActivity.getNextStartedActivity();
                assertEquals(expectedIntent.getComponent(), actual.getComponent());
            }
        });
    }

    @Test
    public void testToDisplayAlertActivityFromAlert(){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout b = mainActivity.findViewById(R.id.relative_layout_alerts);
                b.performClick();

                Intent expectedIntent = new Intent(mainActivity, DisplayAlertActivity.class);
                ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
                Intent actual = shadowActivity.getNextStartedActivity();
                assertEquals(expectedIntent.getComponent(), actual.getComponent());
            }
        });
    }

    @Test
    public void testSelectStationButton(){
        addFavoriteActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout b = addFavoriteActivity.findViewById(R.id.relative_station);
                b.performClick();

                Intent expectedIntent = new Intent(addFavoriteActivity, AllLinesActivity.class);
                ShadowActivity shadowActivity = Shadows.shadowOf(addFavoriteActivity);
                Intent actual = shadowActivity.getNextStartedActivity();
                assertEquals(expectedIntent.getComponent(), actual.getComponent());
            }
        });
    }

    @Test
    public void testToSpecificLineActivity(){
        allLinesActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout b = allLinesActivity.findViewById(R.id.relative_all_lines);
                b.performClick();

                Intent expectedIntent = new Intent(allLinesActivity, SpecificLineActivity.class);
                ShadowActivity shadowActivity = Shadows.shadowOf(allLinesActivity);
                Intent actual = shadowActivity.getNextStartedActivity();
                assertEquals(expectedIntent.getComponent(), actual.getComponent());
            }
        });
    }

    @Test
    public void testToSpecificLineActivityRedLine(){
        allLinesActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout b = allLinesActivity.findViewById(R.id.relative_all_lines);
                b.performClick();

                Intent expectedIntent = new Intent(allLinesActivity, SpecificLineActivity.class);
                expectedIntent.putExtra("line", "Red Line");
                specificlineActivity = Robolectric.buildActivity(SpecificLineActivity.class, expectedIntent).create().start().visible().get();

                TextView tv = specificlineActivity.findViewById(R.id.txt_toolbar_title);
                assertEquals(tv.getText(), "Red Line");
            }
        });
    }

//    @Test
//    public void testToSpecificLineActivity(){
//        allLinesActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                RelativeLayout b = allLinesActivity.findViewById(R.id.relative_all_lines);
//                b.performClick();
//
//                Intent expectedIntent = new Intent(allLinesActivity, SpecificLineActivity.class);
//                ShadowActivity shadowActivity = Shadows.shadowOf(allLinesActivity);
//                Intent actual = shadowActivity.getNextStartedActivity();
//                assertEquals(expectedIntent.getComponent(), actual.getComponent());
//            }
//        });
//    }
}