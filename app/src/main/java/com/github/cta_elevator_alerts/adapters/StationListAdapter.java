package com.github.cta_elevator_alerts.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.MainActivity;
import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for all station lists (Favorites & Alerts) within MainActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.StationListViewHolder> {

    class StationListViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout rl;
        private final ImageView iv;
        private final TextView tv;
        private final View[] lineViews;

        private StationListViewHolder(View itemView) {
            super(itemView);
            rl = itemView.findViewById(R.id.rl_individual_station);
            iv = itemView.findViewById(R.id.img_individual_station);
            tv = itemView.findViewById(R.id.txt_individual_station);

            //Set views for each train line
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
    private final MainViewModel vm;
    private List<Station> stations;

    public StationListAdapter(Context context){
        vm = ((MainActivity)context).getStationAlertsViewModel();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        lineColors = context.getResources().getIntArray(R.array.lineColors);
        stations = new ArrayList<>();
    }

    @Override
    @NonNull
    public StationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.individual_station, parent, false);
        itemView.setBackgroundColor(0x00000000);
        return new StationListViewHolder(itemView);
    }

    public void updateStationList(List<Station> stations){
         this.stations = stations;
         notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StationListViewHolder holder, int position){
        holder.setIsRecyclable(false);
        Station current = stations.get(position);
        boolean[] currentRoutes = vm.getAllRoutes(current.stationID);
        holder.tv.setText(current.name);

        if (vm.getHasElevatorAlert(current.stationID)) {
            holder.iv.setImageResource(R.drawable.status_red);
        } else{
            holder.iv.setVisibility(View.INVISIBLE);
        }

        //populate line bars to show colors of each route under station name
        int viewPosition = 0;
        for(int i = 0; i < currentRoutes.length; i++){
            if(currentRoutes[i]){
                holder.lineViews[viewPosition].setBackgroundColor(lineColors[i]);
                viewPosition++;
            }
        }

        //remove bottom border styling from last element
        if(position == stations.size()-1){
            holder.rl.setBackgroundResource(0);
        } else{
            holder.rl.setBackgroundResource(R.drawable.border_bottom);
        }

        //Make station clickable to see details
        ((View)holder.tv.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayAlertActivity.class);
            intent.putExtra("stationID", current.stationID);
            intent.putExtra("fromMain", true);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return stations.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}