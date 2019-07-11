package com.example.elevator_app.models.database;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {

    class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView favoritesImageView;
        private final TextView favoritesNicknameTextView;
        private final TextView favoritesStationNameTextView;

        private FavoritesAdapterViewHolder(View itemView) {
            super(itemView);
            favoritesImageView = itemView.findViewById(R.id.img_favorite_station);
            favoritesNicknameTextView = itemView.findViewById(R.id.txt_nickname_favorite_station);
            favoritesStationNameTextView = itemView.findViewById(R.id.txt_name_favorite_station);
        }
    }

    private final LayoutInflater mInflater;
    private List<Station> mFavoriteStations;

    public FavoritesAdapter(Context context){ mInflater = LayoutInflater.from(context);}

    @Override
    @NonNull
    public FavoritesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.favorite_station, parent, false);
        return new FavoritesAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapterViewHolder holder, int position){
        if (mFavoriteStations != null){
            Station current = mFavoriteStations.get(position);
            holder.favoritesImageView.setImageResource(R.drawable.status_green);
            holder.favoritesNicknameTextView.setText(current.nickname);
            holder.favoritesStationNameTextView.setText(current.name);
        }
    }

    public void setFavorites(List<Station> stations){
        mFavoriteStations = stations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if (mFavoriteStations != null) return mFavoriteStations.size();
        else return 0;
    }
}


