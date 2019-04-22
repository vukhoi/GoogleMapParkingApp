package com.example.parkinggooglemapapp.model;

import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ParkingApi {

    @GET("api/v1/parkinglocations?format=json")
    Observable<List<Spot>> getSpotList(
            @Query("lat") Double lat,
            @Query("lng") Double lng
    );
}
