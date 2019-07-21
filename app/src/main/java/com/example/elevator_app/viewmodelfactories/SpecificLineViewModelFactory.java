package com.example.elevator_app.viewmodelfactories;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.elevator_app.viewmodels.SpecificLineViewModel;

public class SpecificLineViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final String mParam;

    public SpecificLineViewModelFactory(Application application, String param){
        mApplication = application;
        mParam = param;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new SpecificLineViewModel(mApplication, mParam);
    }


}
