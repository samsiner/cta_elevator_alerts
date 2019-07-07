package com.example.elevator_app.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.elevator_app.R;
import com.example.elevator_app.Models.Stations.Station;

import java.util.HashMap;

public class AddFavoriteActivity extends AppCompatActivity {
    //TODO: Check if user is requesting an already favorite station or same nickname
    //TODO: Check if user is requesting a station with no elevator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        Button button = findViewById(R.id.add_favorite_button);
        setSupportActionBar(toolbar);


//        @SuppressWarnings("unchecked")
//        HashMap<String, Station> allStations = (HashMap<String, Station>) getIntent().getSerializableExtra("allStations");

//        button.setOnClickListener(v -> {
//            //TODO: check for null nickname
//            String nickname = nicknameTextEdit.getText().toString();
//
//            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
//            intent.putExtra("Nickname", nickname);
//            intent.putExtra("stationID", stationID);
//            startActivity(intent);
//        });
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(AddFavoriteActivity.this, AllLinesActivity.class);
        intent.putExtra("allStations", getIntent().getSerializableExtra("allStations"));
        startActivity(intent);
    }

    public void toMainActivity(View v){
        //TODO: check for null nickname
        TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);

        String nickname = nicknameTextEdit.getText().toString();
        Log.d("nickname", nickname);
        String stationID = getIntent().getStringExtra("stationID");

        if(stationID == null){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Error");
            alert.setMessage("Please enter a nickname and select a station");
            alert.setPositiveButton("OK", null);
            alert.show();
            Log.d("error message", "error message failed");
        } else{
            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
            intent.putExtra("Nickname", nickname);
            intent.putExtra("stationID", stationID);
            startActivity(intent);
        }
    }
}
