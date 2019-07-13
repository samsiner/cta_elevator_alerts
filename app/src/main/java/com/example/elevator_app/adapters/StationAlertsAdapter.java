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

        //TODO: there's probably a better way to do this
        int viewPosition = 0;
        if(current.hasRedLine()) {
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorRedLine);
            viewPosition++;
        }
        if(current.hasBlueLine()){
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorBlueLine);
            viewPosition++;
        }
        if(current.hasBrownLine()){
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorBrownLine);
            viewPosition++;
        }
        if(current.hasGreenLine()){
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorGreenLine);
            viewPosition++;
        }
        if(current.hasOrangeLine()) {
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorOrangeLine);
            viewPosition++;
        }
        if(current.hasPinkLine()) {
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorPinkLine);
            viewPosition++;
        }
        if(current.hasPurpleLine()) {
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorPurpleLine);
            viewPosition++;
        }
        if(current.hasYellowLine()) {
            holder.lineViews[viewPosition].setBackgroundResource(R.color.colorYellowLine);
        }

        Log.d("routes", current.getName() + ": " + Arrays.toString(current.getRoutes()));
        //TODO: change to minSDK of 16 instead of 15?
        holder.getView().setBackground(Drawable.createFromPath("drawable/main_activity_containers.xml"));

        //remove bottom border styling from last element
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