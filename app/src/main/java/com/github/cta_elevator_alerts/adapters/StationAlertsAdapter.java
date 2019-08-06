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
import com.github.cta_elevator_alerts.activities.MainActivity;
import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.viewmodels.StationAlertsViewModel;

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
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private final int[] lineColors;
    private final StationAlertsViewModel mStationAlertsViewModel;

    public StationAlertsAdapter(Context context){
        mStationAlertsViewModel = ((MainActivity)context).getStationAlertsViewModel();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        lineColors = context.getResources().getIntArray(R.array.lineColors);
    }

    @Override
    @NonNull
    public StationAlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.alert_station, parent, false);
        return new StationAlertsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StationAlertsViewHolder holder, int position){
        holder.setIsRecyclable(false);
        Station current = mStationAlertsViewModel.mGetStationAlertsNotLiveData().get(position);
        boolean[] currentRoutes = mStationAlertsViewModel.getAllRoutes(current.stationID);
        holder.stationAlertTextView.setText(current.name);

        //populate line bars to show colors of each route under station name
        int viewPosition = 0;
        for(int i = 0; i < currentRoutes.length; i++){
            if(currentRoutes[i]){
                holder.lineViews[viewPosition].setBackgroundColor(lineColors[i]);
                viewPosition++;
            }
        }

        //remove bottom border styling from last element
        if(position == mStationAlertsViewModel.getNumAlerts()-1){
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

    @Override
    public int getItemCount(){
        return mStationAlertsViewModel.getNumAlerts();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}