package com.example.elevator_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
import com.example.elevator_app.activities.DisplayAlertActivity;
import com.example.elevator_app.model.Station;

import java.util.List;

public class StationAlertsAdapter extends RecyclerView.Adapter<StationAlertsAdapter.StationAlertsViewHolder> {

    class StationAlertsViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout stationAlertRelativeLayout;
        private final TextView stationAlertTextView;
        private final View itemView;

        private StationAlertsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            stationAlertRelativeLayout = itemView.findViewById(R.id.relative_layout_alerts);
            stationAlertTextView = itemView.findViewById(R.id.txt_alert_station);
        }

        public View getView(){ return itemView; }
    }

    private final LayoutInflater mInflater;
    private List<Station> mStations;
    private Context context;

    public StationAlertsAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public StationAlertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.alert_station, parent, false);
        return new StationAlertsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationAlertsViewHolder holder, int position){
        Station current = mStations.get(position);
        if (mStations != null){
            holder.stationAlertTextView.setText(current.name);
        }

        //TODO: change to minSDK of 16 instead of 15?
        holder.getView().setBackground(Drawable.createFromPath("drawable/main_activity_containers.xml"));

        if(position == mStations.size()-1){
            holder.stationAlertRelativeLayout.setBackgroundResource(0);
        }

        ((View)holder.stationAlertTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayAlertActivity.class);
            intent.putExtra("stationID", current.stationID);
            context.startActivity(intent);
        });
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


