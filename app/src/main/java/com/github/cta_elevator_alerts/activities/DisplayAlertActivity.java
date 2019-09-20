package com.github.cta_elevator_alerts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.model.StationRepository;
import com.github.cta_elevator_alerts.viewmodels.DisplayAlertViewModel;

/**
 * DisplayAlertActivity shows the details of a
 * specific elevator outage alert
 * (description and date/time it began).
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class DisplayAlertActivity extends AppCompatActivity {
    ImageView starIcon;
    TextView favoriteText;
    Boolean isFavorite;
    String stationID;
    StationRepository mRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert);
        TextView tv_shortDesc = findViewById(R.id.txt_alert_shortDesc);
        TextView toolbarText = findViewById(R.id.txt_toolbar);
        starIcon = this.findViewById(R.id.img_star_icon);
        favoriteText = this.findViewById(R.id.favorited_text);
        mRepository = StationRepository.getInstance(this.getApplication());

        stationID = getIntent().getStringExtra("stationID");

        //Get ViewModel
        DisplayAlertViewModel mDisplayAlertViewModel = ViewModelProviders.of(this).get(DisplayAlertViewModel.class);
        mDisplayAlertViewModel.setStationID(stationID);

        toolbarText.setText(mDisplayAlertViewModel.getStationName()); //Set Station Name

        isFavorite = mDisplayAlertViewModel.getIsFavorite();
        boolean hasElevator = mRepository.mGetHasElevator(stationID);

        if(isFavorite && hasElevator){
            starIcon.setImageResource(R.drawable.star_icon_full);
            favoriteText.setText(R.string.added_to_favorites);
        } else if (hasElevator){
            starIcon.setImageResource(R.drawable.star_icon_empty);
            favoriteText.setText(R.string.add_to_favorites);
        } else {
            starIcon.setVisibility(View.GONE);
            favoriteText.setVisibility(View.GONE);
        }

        //Set alert description
        if (!mDisplayAlertViewModel.getHasElevator()) tv_shortDesc.setText(R.string.no_elevator);
        else if (!mDisplayAlertViewModel.getHasAlert())  tv_shortDesc.setText(R.string.present_elevator);
        else tv_shortDesc.setText(mDisplayAlertViewModel.getShortDesc());
    }

    public void toMainActivity(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        finish();
        startActivity(i);
    }

    public void onBackPressed(View v){ this.onBackPressed(); }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(getIntent().getBooleanExtra("fromMain", false)){
            Intent intent = new Intent(DisplayAlertActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void clickStarIcon(View v){
        if(isFavorite){
            starIcon.setImageResource(R.drawable.star_icon_empty);
            favoriteText.setText(R.string.add_to_favorites);
            mRepository.removeFavorite(stationID);
            isFavorite = false;
        }
        else {
            starIcon.setImageResource(R.drawable.star_icon_full);
            favoriteText.setText(R.string.added_to_favorites);
            mRepository.addFavorite(stationID);
            isFavorite = true;
        }

    }
}
