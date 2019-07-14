package com.example.elevator_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

import java.util.Arrays;
import java.util.List;

public class StationAlertsAdapter extends RecyclerView.Adapter<StationAlertsAdapter.StationAlertsViewHolder> {

    class StationAlertsViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout stationAlertRelativeLayout;
        private final TextView stationAlertTextView;
        private final View itemView;
        private final View[] lineViews;

        private StationAlertsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            stationAlertRelativeLayout = itemView.findViewById(R.id.relative_layout_alerts);
            stationAlertTextView = itemView.findViewById(R.id.txt_alert_station);

            View line_0 = itemView.findViewById(R.id.line_0);
            View line_1 = itemView.findViewById(R.id.line_1);
            View line_2 = itemView.findViewById(R.id.line_2);
            View line_3 = itemView.findViewById(R.id.line_3);
            View line_4 = itemView.findViewById(R.id.line_4);
            View line_5 = itemView.findViewById(R.id.line_5);

            lineViews = new View[]{line_0, line_1, line_2, line_3, line_4, line_5};

        }

        public View getView(){ return itemView; }
    }

    private final LayoutInflater mInflater;
    private List<Station> mStations;
    private Context context;
    private int[] lineColors;

    public StationAlertsAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        lineColors = context.getResources().getIntArray(R.array.lineColors);
    }

    @Override
    public StationAlertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.alert_station, parent, false);
        return new StationAlertsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationAlertsViewHolder holder, int position){
        Station current = mStations.get(position);
        Boolean[] currentRoutes = current.getRoutes();
        holder.stationAlertTextView.setText(current.name);

        //populate line bars under station name
        int viewPosition = 0;
        for(int i = 0; i < currentRoutes.length; i++){
            if(currentRoutes[i]){
                holder.lineViews[viewPosition].setBackgroundColor(lineColors[i]);
                viewPosition++;
            }
        }

        //TODO: change to minSDK of 16 instead of 15?

        //remove bottom border styling from last element
        //TODO: does this work incorrectly when view is 'recycled'?
        if(position == mStations.size()-1){
            holder.stationAlertRelativeLayout.setBackgroundResource(0);
        } else{
            holder.stationAlertRelativeLayout.setBackgroundResource(R.drawable.border_bottom);
        }

        //transparent background
        holder.itemView.setBackgroundColor(0x00000000);

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