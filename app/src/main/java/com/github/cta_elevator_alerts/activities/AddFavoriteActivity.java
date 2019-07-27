package com.github.cta_elevator_alerts.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.cta_elevator_alerts.R;

public class AddFavoriteActivity extends AppCompatActivity {

    private String stationID = "", stationName = "", nickname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        TextView toolbarTextView = findViewById(R.id.txt_toolbar_title);
        toolbarTextView.setText(R.string.add_favorite);

        //If system destroyed activity and recreated it
        if (savedInstanceState != null){
            if (savedInstanceState.getString("stationID") != null) stationID = savedInstanceState.getString("stationID");
            if (savedInstanceState.getString("stationName") != null) stationName = savedInstanceState.getString("stationName");
            if (savedInstanceState.getString("nickname") != null) nickname = savedInstanceState.getString("nickname");
        }

        //Pull in the station that the user selected
        if (getIntent().getStringExtra("stationID") != null) stationID = getIntent().getStringExtra("stationID");
        if (getIntent().getStringExtra("stationName") != null) stationName = getIntent().getStringExtra("stationName");
        if (getIntent().getStringExtra("nickname") != null) nickname = getIntent().getStringExtra("nickname");

        TextView nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
        nicknameTextEdit.setText(nickname);

        TextView addStation = findViewById(R.id.text_add_favorite_station);
        addStation.setText(stationName);
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(AddFavoriteActivity.this, AllLinesActivity.class);
        intent.putExtra("fromFavorites", true);
        intent.putExtra("nickname", getNicknameText());
        startActivity(intent);
    }

    private String getNicknameText(){
        TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
        if (nicknameTextEdit.getText() == null) return "";
        return nicknameTextEdit.getText().toString();
    }

    private void setNicknameText(String input){
        TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
        nicknameTextEdit.setText(input);
    }

    public void toMainActivity(View v) {
        String stationID = getIntent().getStringExtra("stationID");

        if (stationID == null || getNicknameText().equals("")){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Error");
            alert.setMessage("Please enter a nickname and select a station");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else if(getNicknameText().length() >= 20){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Error");
            alert.setMessage("Nickname must be less than 20 characters");
            alert.setPositiveButton("OK", null);
            alert.show();
            setNicknameText("");
        }
        else{
            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
            String text = getNicknameText();
            String capText = text.substring(0,1).toUpperCase() + text.substring(1);
            intent.putExtra("nickname", capText);
            intent.putExtra("stationID", stationID);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nickname", nickname);
        savedInstanceState.putString("stationID", stationID);
        savedInstanceState.putString("stationName", stationName);
    }
}
