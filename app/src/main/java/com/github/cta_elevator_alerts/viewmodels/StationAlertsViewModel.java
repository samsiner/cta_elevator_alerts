package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.github.cta_elevator_alerts.model.APIWorker;
import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StationAlertsViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public List<Station> mGetStationAlertsNotLiveData(){ return mRepository.mGetStationAlertsNotLiveData(); }
    public void rebuildAlerts(){ mRepository.buildAlertsCheckforStationsFirst(); }

    public int getNumAlerts(){ return mRepository.getAlertsCount(); }
    public List<String> getStationElevatorsNewlyWorking(){ return mRepository.getFavoriteElevatorNewlyWorking(); }
    public List<String> getStationElevatorsNewlyOut(){ return mRepository.getFavoriteElevatorNewlyOut(); }
    public String getStationName(String stationID){ return mRepository.mGetStationName(stationID); }
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
    public LiveData<String> getUpdateAlertsTime(){ return mRepository.getUpdateAlertsTime(); }
    public LiveData<Boolean> getConnectionStatus(){ return mRepository.getConnectionStatus(); }
}
