package com.github.cta_elevator_alerts.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.SpecificLineActivity;
import com.github.cta_elevator_alerts.model.Station;

import java.util.List;

public class SpecificLineAlertsAdapter extends RecyclerView.Adapter<SpecificLineAlertsAdapter.SpecificLineAlertsViewHolder>  {

    class SpecificLineAlertsViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout specificLineAlertsRelativeLayout;
        private final TextView stationAlertTextView;
        private final View itemView;

        private SpecificLineAlertsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            specificLineAlertsRelativeLayout = itemView.findViewById(R.id.rl_specific_line_alerts);
            stationAlertTextView = itemView.findViewById(R.id.txt_specific_line_alert_station);
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

        holder.stationAlertTextView.setText(currentName);

        ((View)holder.stationAlertTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayAlertActivity.class);
            intent.putExtra("stationID", currentStationID);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return alertStations.size();
    }
}
