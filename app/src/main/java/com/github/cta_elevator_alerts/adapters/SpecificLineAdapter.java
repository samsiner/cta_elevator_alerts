package com.github.cta_elevator_alerts.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.AddFavoriteActivity;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.SpecificLineActivity;

import java.util.List;

public class SpecificLineAdapter extends RecyclerView.Adapter<SpecificLineAdapter.SpecificLineAdapterViewHolder> {

    class SpecificLineAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView specificLineTextView;
        private final View verticalBarTop;
        private final View verticalBarBottom;
        private final GradientDrawable circle;
        private final ImageView adaImageView;
        private final ImageView statusImageView;
        private final ImageView rightArrow;
        private final ImageView icon_add;

        private SpecificLineAdapterViewHolder(View itemView) {
            super(itemView);
            specificLineTextView = itemView.findViewById(R.id.txt_line_station);
            verticalBarTop = itemView.findViewById(R.id.view_vertical_bar_top);
            verticalBarBottom = itemView.findViewById(R.id.view_vertical_bar_bottom);
            circle = (GradientDrawable)itemView.findViewById(R.id.view_circle).getBackground();
            adaImageView = itemView.findViewById(R.id.img_ada);
            statusImageView = itemView.findViewById(R.id.img_status);
            rightArrow = itemView.findViewById(R.id.img_right);
            icon_add = itemView.findViewById(R.id.img_add_button);
        }

        private void setUI(String lineName, View verticalBarTop, View verticalBarBottom, GradientDrawable circle){
            switch(lineName){
                case("Red Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorRedLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorRedLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorRedLine));
                    break;
                case("Blue Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorBlueLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorBlueLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorBlueLine));
                    break;
                case("Brown Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorBrownLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorBrownLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorBrownLine));
                    break;
                case("Green Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorGreenLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorGreenLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorGreenLine));
                    break;
                case("Orange Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorOrangeLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorOrangeLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorOrangeLine));
                    break;
                case("Pink Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorPinkLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorPinkLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorPinkLine));
                    break;
                case("Purple Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorPurpleLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorPurpleLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorPurpleLine));
                    break;
                case("Yellow Line"):
                    verticalBarTop.setBackgroundResource(R.color.colorYellowLine);
                    verticalBarBottom.setBackgroundResource(R.color.colorYellowLine);
                    circle.setStroke(5, context.getResources().getColor(R.color.colorYellowLine));
                    break;
            }
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private final List<String> lineStations;
    private final androidx.appcompat.widget.Toolbar toolbar;
    private final TextView toolbarTextView;

    public SpecificLineAdapter(Context context, List<String> lineStations){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lineStations = lineStations;
        toolbar = ((Activity)context).findViewById(R.id.toolbar);
        toolbarTextView = ((Activity)context).findViewById(R.id.txt_toolbar);
    }

    @Override
    @NonNull
    public SpecificLineAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.specific_line_station, parent, false);
        return new SpecificLineAdapter.SpecificLineAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificLineAdapter.SpecificLineAdapterViewHolder holder, int position){
        String currStationID = lineStations.get(position);
        String currStationName = ((SpecificLineActivity)context).getStationName(currStationID);
        int transparentColor = context.getResources().getColor(R.color.colorTransparent);
        boolean hasElevator = ((SpecificLineActivity)context).getHasElevator(currStationID);
        boolean fromFavorites = ((Activity)context).getIntent().getBooleanExtra("fromFavorites", false);

        if(fromFavorites){
            holder.rightArrow.setVisibility(View.GONE);
            if(!hasElevator){ holder.icon_add.setVisibility(View.GONE);}
        } else{
            holder.icon_add.setVisibility(View.GONE);
        }
        holder.setUI(toolbarTextView.getText().toString(), holder.verticalBarTop, holder.verticalBarBottom, holder.circle);
        if(position == 0){
            holder.verticalBarTop.setBackgroundColor(transparentColor);
        }
        if(position == lineStations.size() - 1){
            holder.verticalBarBottom.setBackgroundColor(transparentColor);
        }

        holder.specificLineTextView.setText(currStationName);
        if(!hasElevator){
            holder.adaImageView.setImageResource(android.R.color.transparent);
        }
        if(!((SpecificLineActivity) context).getHasElevatorAlert(currStationID)){
            holder.statusImageView.setImageResource(android.R.color.transparent);
        }

        ((View)holder.specificLineTextView.getParent()).setOnClickListener(v -> {
            Intent intent;

            if (fromFavorites) {
                if (!hasElevator) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("No elevator!");
                    alert.setMessage("No elevator is present at this station. Please choose a favorite station with an elevator.");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {
                    intent = new Intent(context, AddFavoriteActivity.class);
                    intent.putExtra("stationID", currStationID);
                    intent.putExtra("stationName", currStationName);
                    intent.putExtra("nickname", ((Activity) context).getIntent().getStringExtra("nickname"));
                    context.startActivity(intent);
                }
            } else{
                intent = new Intent(context, DisplayAlertActivity.class);
                intent.putExtra("stationID", currStationID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        if (lineStations != null) return lineStations.size();
        else return 0;
    }

    public void setToolbar(String lineName){
        int colorID;
        toolbarTextView.setText(lineName);
        switch(lineName){
            case("Red Line"):
                toolbar.setBackgroundResource(R.color.colorRedLine);
                break;
            case("Blue Line"):
                toolbar.setBackgroundResource(R.color.colorBlueLine);
                break;
            case("Brown Line"):
                toolbar.setBackgroundResource(R.color.colorBrownLine);
                break;
            case("Green Line"):
                toolbar.setBackgroundResource(R.color.colorGreenLine);
                break;
            case("Orange Line"):
                toolbar.setBackgroundResource(R.color.colorOrangeLine);
                break;
            case("Pink Line"):
                toolbar.setBackgroundResource(R.color.colorPinkLine);
                break;
            case("Purple Line"):
                toolbar.setBackgroundResource(R.color.colorPurpleLine);
                break;
            case("Yellow Line"):
                colorID = context.getResources().getColor(R.color.colorPrimaryDark);
                toolbar.setBackgroundResource(R.color.colorYellowLine);
                toolbarTextView.setTextColor(colorID);
                break;
        }
    }
}
