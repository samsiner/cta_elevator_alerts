package com.github.cta_elevator_alerts.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.AllLinesActivity;
import com.github.cta_elevator_alerts.activities.SpecificLineActivity;
import com.github.cta_elevator_alerts.viewmodels.AllLinesViewModel;

/**
 * Adapter for AllLinesActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class AllLinesAdapter extends RecyclerView.Adapter<AllLinesAdapter.AllLinesViewHolder> {

    class AllLinesViewHolder extends RecyclerView.ViewHolder {
        private final TextView allLinesTextView;
        private final ImageView trainIconImageView;
        private final ImageView alertIcon;

        private AllLinesViewHolder(View itemView) {
            super(itemView);
            allLinesTextView = itemView.findViewById(R.id.txt_all_lines);
            trainIconImageView = itemView.findViewById(R.id.img_train_icon);
            alertIcon = itemView.findViewById(R.id.img_alert_icon);
        }
    }

    private final LayoutInflater mInflater;
    private final String[] mLines = new String[] {"Red Line", "Blue Line", "Brown Line", "Green Line", "Orange Line", "Pink Line", "Purple Line", "Yellow Line"};
    private final Context context;
    private final TextView toolbarTextView;
    private final AllLinesViewModel mAllLinesViewModel;

    public AllLinesAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        toolbarTextView = ((Activity)context).findViewById(R.id.txt_toolbar);
        mAllLinesViewModel = ((AllLinesActivity)context).getAllLinesViewModel();
    }

    @Override
    @NonNull
    public AllLinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.all_lines, parent, false);
        return new AllLinesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllLinesViewHolder holder, int position){
        String current = mLines[position];
        holder.allLinesTextView.setText(current);
        setTrainIcon(holder.trainIconImageView, current);

        if(mAllLinesViewModel.getAllLineAlerts(current).size() > 0){
            holder.alertIcon.setImageResource(R.drawable.status_red);
        }

        ((View)holder.allLinesTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificLineActivity.class);
            intent.putExtra("line", current);
            intent.putExtra("fromFavorites", (((Activity)context)).getIntent().getBooleanExtra("fromFavorites", false));
            intent.putExtra("nickname", (((Activity)context)).getIntent().getStringExtra("nickname"));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return mLines.length;
    }

    public void setToolbarTextView(){
        toolbarTextView.setText(R.string.all_lines);
    }

    private void setTrainIcon(ImageView imageView, String line){
        switch(line){
            case "Red Line":
                imageView.setImageResource(R.drawable.icon_redline);
                break;
            case "Blue Line":
                imageView.setImageResource(R.drawable.icon_blueline);
                break;
            case "Brown Line":
                imageView.setImageResource(R.drawable.icon_brownline);
                break;
            case "Green Line":
                imageView.setImageResource(R.drawable.icon_greenline);
                break;
            case "Orange Line":
                imageView.setImageResource(R.drawable.icon_orangeline);
                break;
            case "Pink Line":
                imageView.setImageResource(R.drawable.icon_pinkline);
                break;
            case "Purple Line":
                imageView.setImageResource(R.drawable.icon_purpleline);
                break;
            case "Yellow Line":
                imageView.setImageResource(R.drawable.icon_yellowline);
                break;
            default:
                Log.d("Train Icon", "Incorrect input from station lines array");
        }
    }
}