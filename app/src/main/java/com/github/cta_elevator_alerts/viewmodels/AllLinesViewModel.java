package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.List;

public class AllLinesViewModel extends AndroidViewModel {
    public final StationRepository mRepository;
    public AllLinesViewModel(@NonNull Application application) {
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public List<String> getAllLineAlerts(String line){ return mRepository.getAllLineAlerts(line); }
}
