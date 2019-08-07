package com.github.cta_elevator_alerts.viewmodelfactories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.cta_elevator_alerts.viewmodels.DisplayAlertViewModel;

/**
 * Creates DisplayAlertViewModel instance
 * from factory
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

public class DisplayAlertViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final String mParam;

    public DisplayAlertViewModelFactory(Application application, String param){
        mApplication = application;
        mParam = param;
    }

    @Override
    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new DisplayAlertViewModel(mApplication, mParam);
    }


}
