package com.example.elevator_app.models.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
import com.example.elevator_app.activities.DisplayAlertActivity;
import com.example.elevator_app.activities.SpecificLineActivity;

import java.util.List;

public class AllLinesAdapter extends RecyclerView.Adapter<AllLinesAdapter.AllLinesViewHolder> {

    class AllLinesViewHolder extends RecyclerView.ViewHolder {
        private final TextView allLinesTextView;

        private AllLinesViewHolder(View itemView) {
            super(itemView);
            allLinesTextView = itemView.findViewById(R.id.txt_all_lines);
        }
    }

    private final LayoutInflater mInflater;
    private String[] mLines = new String[] {"Red Line", "Blue Line", "Brown Line", "Green Line", "Orange Line", "Pink Line", "Purple Line", "Yellow Line"};
    private Context context;

    public AllLinesAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AllLinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.all_lines, parent, false);
        return new AllLinesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllLinesViewHolder holder, int position){
        String current = mLines[position];
        holder.allLinesTextView.setText(current);

        ((View)holder.allLinesTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificLineActivity.class);
            intent.putExtra("line", current);
            intent.putExtra("fromFavorites", (((Activity)context)).getIntent().getBooleanExtra("fromFavorites", true));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        if (mLines != null) return mLines.length;
        else return 0;
    }
}