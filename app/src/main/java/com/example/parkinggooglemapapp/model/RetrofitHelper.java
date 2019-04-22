package com.example.parkinggooglemapapp.model;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private Retrofit retrofit;
    private ParkingApi parkingApi;

    public RetrofitHelper() {
    }

    public void initializeRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://ridecellparking.herokuapp.com/")
                .build();
        parkingApi = retrofit.create(ParkingApi.class);
    }



    public Observable<List<Spot>> getSurroundingParkingSpots(Double lat, Double lng) {
        Observable<List<Spot>> spotArrayObservable = parkingApi.getSpotList(lat, lng)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return spotArrayObservable;
    }
}
