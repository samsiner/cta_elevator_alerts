package com.example.elevator_app;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView stationsTempOut, favoriteAlerts;
    private LinearLayout linearLayout;
    private Button addFavorite;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    DataFavorites dataFavorites;

    private String[] redLine;
    private String[] blueLine;
    private String[] brownLine;
    private String[] greenLine;
    private String[] orangeLine;
    private String[] pinkLine;
    private String[] purpleLine;
    private String[] yellowLine;

    //TODO: check for duplicate key entry from user
    private ArrayList<String> elevatorOutStationIDs;
    private HashMap<String, Station> allStations = new HashMap<>();
    //TODO: figure out how to store people's favorites in local memory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);
        stationsTempOut = findViewById(R.id.text_elevDownTempList);
        linearLayout = findViewById(R.id.LinearLayout);
        addFavorite = findViewById(R.id.button_addFavorite);
        elevatorOutStationIDs = new ArrayList<>();

        //Set up SharedPreferences
        sharedPref = this.getSharedPreferences("name", MODE_PRIVATE);
        editor = sharedPref.edit();

        //Build train lines
        redLine = new String[]{"40900", "41190", "40100", "41300", "40760", "40880", "41380", "40340", "41200", "40770", "40540", "40080", "41420", "41320", "41220", "40650", "40630", "41450", "40330", "41660", "41090", "40560", "41490", "41400", "41000", "40190", "41230", "41170", "40910", "40990", "40240", "41430", "40450"};
        blueLine = new String[]{"40890", "40820", "40230", "40750", "41280", "41330", "40550", "41240", "40060", "41020", "40570", "40670", "40590", "40320", "41410", "40490", "40380", "40370", "40790", "40070", "41340", "40430", "40350", "40470", "40810", "40220", "40250", "40920", "40970", "40010", "40180", "40980", "40390"};
        brownLine = new String[]{"41290", "41180", "40870", "41010", "41480", "40090", "41500", "41460", "41440", "41310", "40360", "41320", "41210", "40530", "41220", "40660", "40800", "40710", "40460", "40730", "40040", "40160", "40850", "40680", "41700", "40260", "40380"};
        greenLine = new String[]{"40020", "41350", "40610", "41260", "40280", "40700", "40480", "40030", "41670", "41070", "41360", "40170", "41510", "41160", "40380", "40260", "41700", "40680", "41400", "41690", "41120", "40300", "41270", "41080", "40130", "40510", "41140", "40720", "40940", "40290"};
        orangeLine = new String[]{"40930", "40960", "41150", "40310", "40120", "41060", "41130", "41400", "40850", "40160", "40040", "40730", "40380", "40260", "41700", "40680"};
        pinkLine = new String[]{"40580", "40420", "40600", "40150", "40780", "41040", "40440", "40740", "40210", "40830", "41030", "40170", "41510", "41160", "40380", "40260", "41700", "40680", "40850", "40160", "40040", "40730"};
        purpleLine = new String[]{"41050", "41250", "40400", "40520", "40050", "40690", "40270", "40840", "40900", "40540", "41320", "41210", "40530", "41220", "40660", "40800", "40710", "40460", "40380", "40260", "41700", "40680", "40850", "40160", "40040", "40730"};
        yellowLine = new String[]{"40140", "41680", "40900"};

        //TODO: Replace with functionality to add favorites
        //temporary data for testing
//        dataFavorites.addFavorite(new String[]{"Home", "40780"});
//        dataFavorites.addFavorite(new String[]{"Work", "41140"});

        buildButtonClickable();
        buildStations();
        buildAlerts();
        updateFavorites();
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
            editor.putString(nickname, stationID);
            editor.apply();
        }
        LinearLayout favoriteLayout = findViewById(R.id.linear_favorite_stations);
        favoriteLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        HashMap<String, String> favorites = (HashMap) sharedPref.getAll();
        Log.d("keyset", favorites.keySet().toString());
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

//    public void buildFavorites(){
//
//    }

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
            BuildStations bs = new BuildStations(this);
            bs.execute("https://data.cityofchicago.org/resource/8pix-ypme.json").get();
            bs.cancel(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buildAlerts(){
        try{
            BuildAlerts ba = new BuildAlerts(this);
            ba.execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON").get();
            ba.cancel(true);
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
//                linearLayout.addView(textView1);


            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
        }
    }

    private String[] getLine(String string){
        switch(string.toLowerCase()){
            case "red":
                return redLine;
            case "blue":
                return blueLine;
            case "brown":
                return brownLine;
            case "green":
                return greenLine;
            case "orange":
                return orangeLine;
            case "pink":
                return pinkLine;
            case "purple":
                return purpleLine;
            case "yellow":
                return yellowLine;
            default:
                return new String[]{"Incorrect Line"};
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