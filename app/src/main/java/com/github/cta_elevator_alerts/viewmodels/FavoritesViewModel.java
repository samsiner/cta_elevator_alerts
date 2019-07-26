package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public FavoritesViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public androidx.lifecycle.LiveData<List<Station>> getFavorites() {
        return mRepository.mGetAllFavorites();
    }

    public List<Station> getFavoritesNotLiveData(){
        return mRepository.mGetAllFavoritesNotLiveData();
    }

    public int getNumFavorites(){ return mRepository.getFavoritesCount();}
    public void addFavorite(String stationID, String nickname){ mRepository.addFavorite(stationID, nickname);}
    public void removeFavorite(String stationID){ mRepository.removeFavorite(stationID);}
    public boolean getHasElevatorAlert(String stationID){ return mRepository.mGetHasElevatorAlert(stationID);}
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}

}


