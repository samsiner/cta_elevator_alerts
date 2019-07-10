package com.example.elevator_app.models.database;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private LiveData<List<Station>> mAllFavoriteStations;

    public FavoritesViewModel(Application application){
        super(application);
        StationRepository mRepository = StationRepository.getInstance(application);
        mAllFavoriteStations = mRepository.mGetAllFavorites();
    }

    public androidx.lifecycle.LiveData<List<Station>> getFavorites() { return mAllFavoriteStations;}
}


