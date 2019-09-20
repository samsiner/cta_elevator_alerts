package com.github.cta_elevator_alerts.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.SpecificLineActivity;

import java.util.List;

/**
 * Adapter for alerts within SpecificLineActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class SpecificLineAlertsAdapter extends RecyclerView.Adapter<SpecificLineAlertsAdapter.SpecificLineAlertsViewHolder>  {

    class SpecificLineAlertsViewHolder extends RecyclerView.ViewHolder {
        private final TextView stationAlertTextView;
        private final ImageView star_icon;

        private SpecificLineAlertsViewHolder(View itemView) {
            super(itemView);
            stationAlertTextView = itemView.findViewById(R.id.txt_specific_line_alert_station);
            star_icon = itemView.findViewById(R.id.img_star_icon);
        }
    }

    private final Context context;
    private final List<String> alertStations;
    private final LayoutInflater mInflater;

    public SpecificLineAlertsAdapter(Context context, List<String> alertStations){
        this.context = context;
        this.alertStations = alertStations;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SpecificLineAlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.specific_line_alert_station, parent, false);
        return new SpecificLineAlertsAdapter.SpecificLineAlertsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificLineAlertsViewHolder holder, int position) {
        String currentStationID = alertStations.get(position);
        String currentName = ((SpecificLineActivity)context).getStationName(currentStationID);
        boolean isFavorite = ((SpecificLineActivity)context).getIsFavorite(currentStationID);

        holder.stationAlertTextView.setText(currentName);

        ((View)holder.stationAlertTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayAlertActivity.class);
            intent.putExtra("stationID", currentStationID);
            context.startActivity(intent);
        });

        if(isFavorite){
            holder.star_icon.setVisibility(View.VISIBLE);
        } else{
            holder.star_icon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return alertStations.size();
    }
}
