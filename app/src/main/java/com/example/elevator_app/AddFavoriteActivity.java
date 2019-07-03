package com.example.elevator_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.HashMap;

public class AddFavoriteActivity extends AppCompatActivity {
    //TODO: Check if user is requesting an already favorited station or same nickname
    //TODO: Check if user is requesting a station with no elevator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        final RadioGroup radiogroup = findViewById(R.id.choose_station_radio_group);
        Button button = findViewById(R.id.add_favorite_button);
        final TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        //TODO: What is this unchecked cast warning?
        @SuppressWarnings("unchecked")
        int counter = 0;
        HashMap<String, Station> allStations = AllStationsSingleton.getInstance().getAllStations();
        for (String str : allStations.keySet()){
            RadioButton r = new RadioButton(AddFavoriteActivity.this);
            r.setText(allStations.get(str).getName());
            r.setId(Integer.parseInt(str));
            radiogroup.addView(r);
            counter++;
            //TODO: just for now
            if(counter == 5){ break; }

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check for null nickname
                String nickname = nicknameTextEdit.getText().toString();
                String stationID = Integer.toString(radiogroup.getCheckedRadioButtonId());

                TextView tv = findViewById(R.id.confirm_favorite_textview);
                tv.append("You have added " + nickname + " as a favorite station!");

                Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
                intent.putExtra("Nickname", nickname);
                intent.putExtra("stationID", stationID);
                startActivity(intent);
            }
        });




    }
}
