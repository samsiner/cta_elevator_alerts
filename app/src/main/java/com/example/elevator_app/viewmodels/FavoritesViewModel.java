package com.example.elevator_app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.elevator_app.model.Station;
import com.example.elevator_app.model.StationRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public FavoritesViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public androidx.lifecycle.LiveData<List<Station>> getFavorites() { return mRepository.mGetAllFavorites();}

    public int getNumFavorites(){ return mRepository.getFavoritesCount();}

    public void addFavorite(String stationID, String nickname){ mRepository.addFavorite(stationID, nickname);}

    public void removeFavorite(String stationID){ mRepository.removeFavorite(stationID);}
}


