package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.elevator_app.models.stations.AllStations;
import com.example.elevator_app.R;

public class AddFavoriteActivity extends AppCompatActivity {
    //TODO: Check if user is requesting an already favorite station or same nickname
    //TODO: Check if user is requesting a station with no elevator
    //TODO: Keep nickname in textview after station is selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);
        String stationID = getIntent().getStringExtra("stationID");

        if(stationID != null){
            TextView stationName = findViewById(R.id.text_add_favorite_station);
            AllStations allStations = (AllStations) getIntent().getSerializableExtra("allStations");
            Log.d("allStations size", Integer.toString(allStations.getAllStations().size()));
            //stationName.setText(allStations.getStation(stationID).getName());
        }
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(AddFavoriteActivity.this, AllLinesActivity.class);
        intent.putExtra("allStations", getIntent().getSerializableExtra("allStations"));
        intent.putExtra("fromFavorites", true);
        startActivity(intent);
    }

    public void toMainActivity(View v) {
        //TODO: check for null nickname
        TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);

        String nickname = nicknameTextEdit.getText().toString();
        String stationID = getIntent().getStringExtra("stationID");

        if(stationID == null){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Error");
            alert.setMessage("Please enter a nickname and select a station");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else{
            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
            intent.putExtra("Nickname", nickname);
            intent.putExtra("stationID", stationID);
            startActivity(intent);
        }
    }
}
