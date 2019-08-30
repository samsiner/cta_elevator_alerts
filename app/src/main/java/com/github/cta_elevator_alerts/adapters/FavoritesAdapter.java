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

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.AddFavoriteActivity;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.activities.MainActivity;
import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.viewmodels.FavoritesViewModel;

/**
 * Adapter for favorites in MainActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class FavoritesAdapter extends RecyclerSwipeAdapter<FavoritesAdapter.FavoritesAdapterViewHolder> {

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder {
        private final SwipeLayout swipeLayout;
        private final RelativeLayout rl_edit;
        private final RelativeLayout rl_delete;
        private final RelativeLayout favoritesLayout;
        private final ImageView favoritesImageView;
        private final TextView favoritesNicknameTextView;
        private final TextView favoritesStationNameTextView;
        private final View[] lineViews;
        private final RelativeLayout hamburger;

        private FavoritesAdapterViewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe);
            rl_edit = itemView.findViewById(R.id.rl_edit);
            rl_delete = itemView.findViewById(R.id.rl_delete);
            favoritesLayout = itemView.findViewById(R.id.relative_layout_favorites);
            favoritesImageView = itemView.findViewById(R.id.img_favorite_station);
            favoritesNicknameTextView = itemView.findViewById(R.id.txt_nickname_favorite_station);
            favoritesStationNameTextView = itemView.findViewById(R.id.txt_name_favorite_station);
            hamburger = itemView.findViewById(R.id.rl_hamburger);

            View line_0 = itemView.findViewById(R.id.favorite_line_0);
            View line_1 = itemView.findViewById(R.id.favorite_line_1);
            View line_2 = itemView.findViewById(R.id.favorite_line_2);
            View line_3 = itemView.findViewById(R.id.favorite_line_3);
            View line_4 = itemView.findViewById(R.id.favorite_line_4);
            View line_5 = itemView.findViewById(R.id.favorite_line_5);

            lineViews = new View[]{line_0, line_1, line_2, line_3, line_4, line_5};
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private final FavoritesViewModel mFavoritesViewModel;
    private final int[] lineColors;
    private final SwipeItemRecyclerMangerImpl mItemManager;

    public FavoritesAdapter(Context context){
        mFavoritesViewModel = ((MainActivity)context).getFavoritesViewModel();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        lineColors = context.getResources().getIntArray(R.array.lineColors);
        mItemManager = new SwipeItemRecyclerMangerImpl(this);
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
        holder.setIsRecyclable(false);
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
            intent.putExtra("fromMain", true);
            context.startActivity(intent);
        });

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        //drag from right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wrapper));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener(){
            @Override
            public void onClose(SwipeLayout layout){
                //totally closed
            }
            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset){
                //swiping
            }
            @Override
            public void onStartOpen(SwipeLayout layout){
                //begin opening
            }
            @Override
            public void onOpen(SwipeLayout layout){
                //completely open
            }
            @Override
            public void onStartClose(SwipeLayout layout){
                //begin closing
            }
            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel){
                //hand released
            }

        });

        holder.rl_delete.setOnClickListener(view -> removeFavoriteStation(position, holder));

        holder.rl_edit.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddFavoriteActivity.class);
            intent.putExtra("nickname", current.nickname);
            intent.putExtra("stationName", current.name);
            intent.putExtra("stationID", current.stationID);
            intent.putExtra("fromEdit", true);

            context.startActivity(intent);
        });

        mItemManager.bindView(holder.itemView, position);

        holder.hamburger.setOnClickListener(v -> {
            if(holder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Close){
                holder.swipeLayout.open(true);
            } else{
                holder.swipeLayout.close(true);
            }
        });
    }

    @Override
    public int getItemCount(){
        return mFavoritesViewModel.getNumFavorites();
    }

    private void removeFavoriteStation(int position, FavoritesAdapterViewHolder holder){
        Station s = mFavoritesViewModel.getFavoritesNotLiveData().get(position);
        mFavoritesViewModel.removeFavorite(s.stationID);
        holder.swipeLayout.close(false);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}