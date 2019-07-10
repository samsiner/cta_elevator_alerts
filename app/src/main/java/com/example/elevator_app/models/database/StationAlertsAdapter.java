package com.example.elevator_app.models.database;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;

import java.util.List;

public class StationAlertsAdapter extends RecyclerView.Adapter<StationAlertsAdapter.StationAlertsViewHolder> {

    class StationAlertsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView stationAlertImageView;
        private final TextView stationAlertTextView;

        private StationAlertsViewHolder(View itemView) {
            super(itemView);
            stationAlertImageView = itemView.findViewById(R.id.img_alert_station);
            stationAlertTextView = itemView.findViewById(R.id.txt_alert_station);
        }
    }

    private final LayoutInflater mInflater;
    private List<Station> mStations;

    public StationAlertsAdapter(Context context){ mInflater = LayoutInflater.from(context);}

    @Override
    @NonNull
    public StationAlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.alert_station, parent, false);
        return new StationAlertsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StationAlertsViewHolder holder, int position){
        if (mStations != null){
            Station current = mStations.get(position);
            holder.stationAlertImageView.setImageResource(R.drawable.status_green);
            holder.stationAlertTextView.setText(current.name);

//            holder.st.setOnClickListener(v -> {
//                Intent intent = new Intent(MainActivity.this, DisplayAlertActivity.class);
//                intent.putExtra("Station", s);
//                startActivity(intent);
//            });
        }
    }

    public void setStations(List<Station> stations){
        mStations = stations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if (mStations != null) return mStations.size();
        else return 0;
    }
}


