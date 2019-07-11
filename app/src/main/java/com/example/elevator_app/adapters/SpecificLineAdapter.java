package com.example.elevator_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
import com.example.elevator_app.activities.AddFavoriteActivity;
import com.example.elevator_app.activities.DisplayAlertActivity;
import com.example.elevator_app.activities.SpecificLineActivity;
import com.example.elevator_app.viewmodels.SpecificLineViewModel;

public class SpecificLineAdapter extends RecyclerView.Adapter<SpecificLineAdapter.SpecificLineAdapterViewHolder> {

    class SpecificLineAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView specificLineTextView;

        private SpecificLineAdapterViewHolder(View itemView) {
            super(itemView);
            specificLineTextView = itemView.findViewById(R.id.txt_line_station);
        }
    }

    private final LayoutInflater mInflater;
    private Context context;
    private String[] lineStations;

    public SpecificLineAdapter(Context context, String[] stations){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        lineStations = stations;
    }

    @Override
    public SpecificLineAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.specific_line_station, parent, false);
        return new SpecificLineAdapter.SpecificLineAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecificLineAdapter.SpecificLineAdapterViewHolder holder, int position){
        String currStationID = lineStations[position];
        holder.specificLineTextView.setText(((SpecificLineActivity)context).getStationName(currStationID));

        ((View)holder.specificLineTextView.getParent()).setOnClickListener(v -> {
            boolean fromFavorites = ((Activity)context).getIntent().getBooleanExtra("fromFavorites", false);

            Intent intent;
            if (fromFavorites){
                intent = new Intent(context, AddFavoriteActivity.class);
                intent.putExtra("stationID", currStationID);
            } else{
                intent = new Intent(context, DisplayAlertActivity.class);
                intent.putExtra("stationID", currStationID);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        if (lineStations != null) return lineStations.length;
        else return 0;
    }
}
