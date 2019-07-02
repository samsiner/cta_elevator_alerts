package com.example.elevator_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button addFavorite;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    //TODO: check for duplicate key entry from user
    private ArrayList<String> elevatorOutStationIDs;
    private HashMap<String, Station> allStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);
        addFavorite = findViewById(R.id.button_addFavorite);
        elevatorOutStationIDs = new ArrayList<>();
        allStations = new HashMap<>();

        //Set up SharedPreferences
        sharedPref = this.getSharedPreferences("name", MODE_PRIVATE);

        buildButtonClickable();
        buildStations();
        buildLines();
        updateFavorites();
        updateAlerts();
    }

    public HashMap<String, Station> getAllStations(){ return allStations; }
    public ArrayList<String> getElevatorOutStationIDs(){ return elevatorOutStationIDs;}

    public void buildButtonClickable(){
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFavoriteActivity.class);
                intent.putExtra("AllStations", allStations);
                startActivity(intent);
            }
        });
    }

    public void updateFavorites(){
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("Nickname");
        String stationID = intent.getStringExtra("stationID");
        if(nickname != null){
            editor = sharedPref.edit();
            editor.putString(nickname, stationID);
            editor.apply();
        }
        LinearLayout favoriteLayout = findViewById(R.id.linear_favorite_stations);
        favoriteLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        @SuppressWarnings("unchecked")
        HashMap<String, ?> favorites = (HashMap) sharedPref.getAll();
        for (String s : favorites.keySet())
        {
            try{
                View myLayout = inflater.inflate(R.layout.favorite_station, favoriteLayout, false);
                TextView nicknameView = myLayout.findViewById(R.id.text_favorite_nickname);
                TextView station = myLayout.findViewById(R.id.text_favorite_station);
                ImageView status = myLayout.findViewById(R.id.image_elev_status);

                nicknameView.setText(s);
                station.setText(allStations.get(favorites.get(s)).getName());
                status.setImageResource(R.drawable.status_green);

                favoriteLayout.addView(myLayout);

            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

//    public String elevatorStatus(String ID){
//        boolean hasElevator = allStations.get(ID).getElevator();
//
//        if (!hasElevator){
//            return "Status: No elevator present\n\n";
//        }
//        else if (elevatorOutStationIDs.contains(ID)){
//            return "Status: Elevator temporarily not working\n\n";
//        }
//        else {
//            return "Status: All elevators working\n\n";
//        }
//    }

    public void buildStations(){
        //TODO: build stations into local database
        //TODO: organize routes to reflect order on CTA site
        try{
            AllStations bs = new AllStations();
            bs.execute("https://data.cityofchicago.org/resource/8pix-ypme.json").get();
            bs.cancel(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buildLines(){
        AllLines lines = new AllLines();
    }

    public void updateAlerts(){
        try{
            AllAlerts aa = new AllAlerts(this);
            aa.execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON").get();
            aa.cancel(true);
            displayAlerts();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayAlerts() {
        for (String str : elevatorOutStationIDs) {
            //TODO: check to make sure station still exists in allStations
            //Create new TextView with the alert
            try {
                final Station s = allStations.get(str);
                TextView textView1 = new TextView(this);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                textView1.setTextSize(15);
                textView1.append("Station: " + s.getName() + "\n");
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DisplayAlertActivity.class);
                        intent.putExtra("Station", s);
                        startActivity(intent);
                    }
                });
                textView1.setId(Integer.parseInt(str));

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}