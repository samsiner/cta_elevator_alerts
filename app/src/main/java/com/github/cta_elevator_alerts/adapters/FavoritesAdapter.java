package com.github.cta_elevator_alerts.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.github.cta_elevator_alerts.viewmodels.FavoritesViewModel;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {

    class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout favoritesLayout;
        private final ImageView favoritesImageView;
        private final TextView favoritesNicknameTextView;
        private final TextView favoritesStationNameTextView;
        private final View[] lineViews;

        private FavoritesAdapterViewHolder(View itemView) {
            super(itemView);
            favoritesLayout = itemView.findViewById(R.id.relative_layout_favorites);
            favoritesImageView = itemView.findViewById(R.id.img_favorite_station);
            favoritesNicknameTextView = itemView.findViewById(R.id.txt_nickname_favorite_station);
            favoritesStationNameTextView = itemView.findViewById(R.id.txt_name_favorite_station);

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
    private final FavoritesViewModel mFavoritesViewModel;
    private final int[] lineColors;

    public FavoritesAdapter(Context context){
        mFavoritesViewModel = ((MainActivity)context).getFavoritesViewModel();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        lineColors = context.getResources().getIntArray(R.array.lineColors);
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
        Station current = mFavoritesViewModel.getFavoritesNotLiveData().get(position);
        boolean[] currentRoutes = mFavoritesViewModel.getAllRoutes(current.stationID);
        holder.favoritesNicknameTextView.setText(current.nickname);
        holder.favoritesStationNameTextView.setText(current.name);

        if (mFavoritesViewModel.getHasElevatorAlert(current.stationID)) {
            holder.favoritesImageView.setImageResource(R.drawable.status_red);
        } else {
            holder.favoritesImageView.setImageResource(R.drawable.status_green);
        }

        //populate line bars to show colors of each route under station name
        int viewPosition = 0;
        for(int i = 0; i < currentRoutes.length; i++){
            if(currentRoutes[i]){
                holder.lineViews[viewPosition].setBackgroundColor(lineColors[i]);
                viewPosition++;
            }
        }

        //remove bottom border on last item
        if (position == mFavoritesViewModel.getNumFavorites() - 1) {
            holder.favoritesLayout.setBackgroundResource(0);
        } else {
            holder.favoritesLayout.setBackgroundResource(R.drawable.border_bottom);
        }

        //Make favorite station clickable to see alert details
        ((View) holder.favoritesNicknameTextView.getParent()).setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayAlertActivity.class);
            intent.putExtra("stationID", current.stationID);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return mFavoritesViewModel.getNumFavorites();
    }

    public void onItemDismiss(int position){
        Station s = mFavoritesViewModel.getFavoritesNotLiveData().get(position);
        mFavoritesViewModel.removeFavorite(s.stationID);
        notifyDataSetChanged();
    }
}