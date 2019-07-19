package com.example.elevator_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
import com.example.elevator_app.activities.DisplayAlertActivity;
import com.example.elevator_app.activities.MainActivity;
import com.example.elevator_app.model.Station;
import com.example.elevator_app.viewmodels.FavoritesViewModel;

import java.util.Collections;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {

    class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final RelativeLayout favoritesLayout;
        private final ImageView favoritesImageView;
        private final TextView favoritesNicknameTextView;
        private final TextView favoritesStationNameTextView;

        private FavoritesAdapterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            favoritesLayout = itemView.findViewById(R.id.relative_layout_favorites);
            favoritesImageView = itemView.findViewById(R.id.img_favorite_station);
            favoritesNicknameTextView = itemView.findViewById(R.id.txt_nickname_favorite_station);
            favoritesStationNameTextView = itemView.findViewById(R.id.txt_name_favorite_station);
        }

        public View getView(){return itemView;}
    }

    private final LayoutInflater mInflater;
    private List<Station> mFavoriteStations;
    private Context context;
    private FavoritesViewModel mFavoritesViewModel;

    public FavoritesAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        mFavoritesViewModel = ((MainActivity)context).getFavoritesViewModel();
    }

    @Override
    @NonNull
    public FavoritesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.favorite_station, parent, false);
        itemView.setBackgroundColor(0x00000000);
        return new FavoritesAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapterViewHolder holder, int position){
        Station current = mFavoriteStations.get(position);
        holder.favoritesNicknameTextView.setText(current.nickname);
        holder.favoritesStationNameTextView.setText(current.name);

        if(current.hasElevatorAlert()){
            holder.favoritesImageView.setImageResource(R.drawable.status_red);
        } else {
            holder.favoritesImageView.setImageResource(R.drawable.status_green);
        }

        //remove bottom border on last item
        if(position == mFavoriteStations.size()-1){
            holder.favoritesLayout.setBackgroundResource(0);
        } else{
            holder.favoritesLayout.setBackgroundResource(R.drawable.border_bottom);
        }

        ((View)holder.favoritesNicknameTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayAlertActivity.class);
            intent.putExtra("stationID", current.stationID);
            context.startActivity(intent);
        });
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

//    public boolean onItemMove(int fromPosition, int toPosition){
//        if(fromPosition < toPosition){
//            for(int i = fromPosition; i < toPosition; i++){
//                Collections.swap()
//            }
//        }
//    }

    public void onItemDismiss(int position){
        Station s = mFavoriteStations.get(position);
        mFavoritesViewModel.removeFavorite(s.stationID);
    }
}