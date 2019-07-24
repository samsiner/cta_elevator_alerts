package com.example.elevator_app.viewmodelfactories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.elevator_app.viewmodels.DisplayAlertViewModel;

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
