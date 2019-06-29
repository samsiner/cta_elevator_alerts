package com.example.elevator_app;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
    private String nickname;
    private Station station;


    public void setNickname(String nickname){ this.nickname = nickname; }
    public void setStation(Station station){ this.station = station; }
    public String getNickname(){ return nickname; }
    public Station getStation(){ return station; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
