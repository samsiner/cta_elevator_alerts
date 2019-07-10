package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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


