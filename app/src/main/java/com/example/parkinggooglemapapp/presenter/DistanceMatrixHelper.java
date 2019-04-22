package com.example.parkinggooglemapapp.presenter;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.Unit;

public class DistanceMatrixHelper {
    public static int SUCCESS = 1;
    public static int FAILURE = 0;
    private String API_KEY = "AIzaSyCEMC-ndDPuAyFWkfnkNtvSJ8O1ZeDJzPE";
    private GeoApiContext context = new GeoApiContext
            .Builder()
            .apiKey(API_KEY)
            .build();
    private DistanceMatrixApiRequest distanceMatrixApiRequest = new DistanceMatrixApiRequest(context);


    public void fetchDistanceToLocation(LatLng currentLatLng, LatLng spotLatLng, final ResultReceiver resultReceiver, final Bundle dialogFields, final com.google.android.gms.maps.model.LatLng key){

        distanceMatrixApiRequest
                .origins(currentLatLng)
                .destinations(spotLatLng)
                .units(Unit.METRIC);


        distanceMatrixApiRequest.setCallback(new PendingResult.Callback<DistanceMatrix>() {
            @Override
            public void onResult(DistanceMatrix result) {

                try {
                    String distance = result.
                            rows[0].
                            elements[0].
                            distance.
                            humanReadable;

                    Bundle bundle = dialogFields;
                    bundle.putString(MapActivityPresenter.DISTANCE, distance);
                    bundle.putParcelable(MapActivityPresenter.KEY, key);
                    Log.d("TEST", "result " + result.rows[0].elements[0]);
                    resultReceiver.send(SUCCESS, bundle);
                }
                catch (NullPointerException e){
                    Log.d("TEST", "failure");
                    resultReceiver.send(FAILURE, null);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d("TEST", "failure");
                e.printStackTrace();
                Log.e(this.getClass().getSimpleName(), e.getMessage());
                resultReceiver.send(FAILURE,  null);
            }
        });
    }
}
