package com.example.elevator_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
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

    public SpecificLineAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public SpecificLineAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.all_lines, parent, false);
        return new SpecificLineAdapter.SpecificLineAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecificLineAdapter.SpecificLineAdapterViewHolder holder, int position){
        String line = ((Activity)context).getIntent().getStringExtra("line");
        //TODO: write specific lines
        holder.specificLineTextView.setText(line);

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
