package com.example.elevator_app;

import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DataFavorites implements Parcelable{
    private ArrayList<String[]> favorites = new ArrayList<>();

    public DataFavorites(Parcel in){

    }

    public void addFavorite(String[] favorite){ favorites.add(favorite); }
    public void removeFavorite(String[] favorite){ favorites.remove(favorite); }
    public ArrayList<String[]> getFavorites(){ return favorites; }
    public int describeContents(){ return 0; }
    public void writeToParcel(Parcel dest, int flags){

    }
}
