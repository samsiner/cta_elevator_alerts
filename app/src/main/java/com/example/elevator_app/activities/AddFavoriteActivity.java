package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.elevator_app.R;

public class AddFavoriteActivity extends AppCompatActivity {
    //TODO: Check if user is requesting an already favorite station or same nickname
    //TODO: Check if user is requesting a station with no elevator
    //TODO: Keep nickname in textview after station is selected

    private String nickname, stationID, stationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        TextView toolbarTextView = findViewById(R.id.txt_toolbar_title);
        toolbarTextView.setText(R.string.add_favorite);
        stationID = getIntent().getStringExtra("stationID");
        stationName = getIntent().getStringExtra("stationName");

        if (stationID != null && stationName != null){
            TextView addStation = findViewById(R.id.text_add_favorite_station);
            addStation.setText(stationName);
        }
        //TODO: if either is null, do something
    }

    //TODO: Display station

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(AddFavoriteActivity.this, AllLinesActivity.class);
        intent.putExtra("fromFavorites", true);
        startActivity(intent);
    }

    public void toMainActivity(View v) {
        //TODO: check for null nickname
        TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);

        String nickname = nicknameTextEdit.getText().toString();
        String stationID = getIntent().getStringExtra("stationID");

        if (stationID == null){
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
