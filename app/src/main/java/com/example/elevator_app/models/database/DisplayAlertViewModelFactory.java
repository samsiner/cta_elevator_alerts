package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DisplayAlertViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String mParam;

    public DisplayAlertViewModelFactory(Application application, String param){
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new DisplayAlertViewModel(mApplication, mParam);
    }


}
