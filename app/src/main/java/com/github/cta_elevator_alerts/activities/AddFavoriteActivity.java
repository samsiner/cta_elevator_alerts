package com.github.cta_elevator_alerts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.cta_elevator_alerts.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddFavoriteActivity extends AppCompatActivity {

    private String stationID = "", stationName = "", nickname = "";
    private Button addFavoriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);
        addFavoriteBtn = this.findViewById(R.id.add_favorite_button);
        TextView toolbarTextView = findViewById(R.id.txt_toolbar_title);

        if (getIntent().getBooleanExtra("fromEdit", false)) {
            toolbarTextView.setText(R.string.update_favorite);
            addFavoriteBtn.setText(R.string.update_favorite);
        } else{
            toolbarTextView.setText(R.string.add_favorite);
        }

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

        //change UI of button based on text edit input
        nicknameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    removeClickableUI(addFavoriteBtn);
                } else{
                    if(!stationName.equals("")){
                        addClickableUI(addFavoriteBtn);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TextView addStation = findViewById(R.id.text_add_favorite_station);
        addStation.setText(stationName);

        if(!stationName.equals("") && !stationID.equals("") && !nickname.equals("")){ addClickableUI(addFavoriteBtn); }
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
            TextInputEditText nicknameTextEdit = findViewById(R.id.inputNickname_textedit);
            nicknameTextEdit.setText("");        }
        else{
            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
            String text = getNicknameText().trim();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(getIntent().getBooleanExtra("fromEdit", false)){
            Intent intent = new Intent(AddFavoriteActivity.this, MainActivity.class);
            intent.putExtra("nickname", getIntent().getStringExtra("nickname"));
            intent.putExtra("stationID", getIntent().getStringExtra("stationID"));
            startActivity(intent);
        }
    }

    private void addClickableUI(Button b){
        b.setBackgroundResource(0);
        b.setBackgroundColor(getResources().getColor(R.color.colorAndroidGreen));
        b.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void removeClickableUI(Button b){
        b.setBackground(getResources().getDrawable(R.drawable.btn_outline));
        b.setTextColor(getResources().getColor(R.color.colorGray));
    }
}
