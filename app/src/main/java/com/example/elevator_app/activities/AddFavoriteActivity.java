package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.elevator_app.R;

public class AddFavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        TextView toolbarTextView = findViewById(R.id.txt_toolbar_title);
        toolbarTextView.setText(R.string.add_favorite);
        String stationID = getIntent().getStringExtra("stationID");
        String stationName = getIntent().getStringExtra("stationName");
        setNicknameText(getIntent().getStringExtra("nickname"));

        if (stationID != null && stationName != null){
            TextView addStation = findViewById(R.id.text_add_favorite_station);
            addStation.setText(stationName);
        }
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(AddFavoriteActivity.this, AllLinesActivity.class);
        intent.putExtra("fromFavorites", true);
        intent.putExtra("nickname", getNicknameText());
        startActivity(intent);
    }

    private String getNicknameText(){
        TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
        try{
            return nicknameTextEdit.getText().toString();
        } catch (NullPointerException e){
            return "";
        }
    }

    private void setNicknameText(String s){
        TextView nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
        nicknameTextEdit.setText(s);
    }

    public void toMainActivity(View v) {
        String stationID = getIntent().getStringExtra("stationID");

        if (stationID == null || getNicknameText().equals("")){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Error");
            alert.setMessage("Please enter a nickname and select a station");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else{
            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
            intent.putExtra("nickname", getNicknameText());
            intent.putExtra("stationID", stationID);
            startActivity(intent);
        }
    }
}
