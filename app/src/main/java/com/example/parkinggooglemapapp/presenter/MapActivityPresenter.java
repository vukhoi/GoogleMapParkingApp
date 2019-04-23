package com.example.parkinggooglemapapp.presenter;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.parkinggooglemapapp.model.RetrofitHelper;
import com.example.parkinggooglemapapp.model.Spot;
import com.example.parkinggooglemapapp.view.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapActivityPresenter {
    public static String NAME = "NAME";
    public static String ADDR = "ADDR";
    public static String COST = "COST";
    public static String DISTANCE = "DISTANCE";
    public static String KEY = "KEY";
    private String LOG_TAG = this.getClass().getSimpleName();
    private int NUM_SPOTS_GET = 10;
    private Context context;
    private GoogleMap map;
    private RetrofitHelper retrofitHelper;
    private Map<LatLng, CustomDialogPayAndReserve> dialogMap = new HashMap<>();
    private Integer waitCounter = NUM_SPOTS_GET;


    public MapActivityPresenter(Context context, GoogleMap map){
        this.context = context;
        this.map = map;
        retrofitHelper = new RetrofitHelper();
        retrofitHelper.initializeRetrofit();
    }

    public void changeMapFocus(List<LatLng> latLngList){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Log.d("latlng size", latLngList.size()+ "");
        for (LatLng latLng: latLngList){
            builder.include(latLng);
        }
        int padding = 0;
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), padding));
    }


    public void searchSurroundingParkingSpots(final Double lat, final Double lng){
        final com.google.maps.model.LatLng currentLatLng = new com.google.maps.model.LatLng(lat, lng);

        Observable<List<Spot>> spotArrayObservable = retrofitHelper.getSurroundingParkingSpots(lat, lng);
        spotArrayObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Spot>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG_TAG, " onSubscribe");
                    }

                    @Override
                    public void onNext(List<Spot> spots) {
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        Log.d(LOG_TAG, " onNext");


                        int count = 0;
                        for (Spot spot : spots){
                            if (spot != null && count < NUM_SPOTS_GET){
                                Log.d(LOG_TAG, spot.getName() + " " + spot.getCostPerMinute());
                                LatLng coordinate = new LatLng(Double.parseDouble(spot.getLat()), Double.parseDouble(spot.getLng()));
                                MarkerOptions markerOptions = new MarkerOptions().position(coordinate);
                                map.addMarker(markerOptions);


                                count += 1;
                                String name = spot.getName();
                                String address = getLocationAddr(spot);
                                String cost = spot.getCostPerMinute();
                                Bundle dialogFields = new Bundle();
                                dialogFields.putString(NAME, name);
                                dialogFields.putString(ADDR, address);
                                dialogFields.putString(COST, cost);
                                com.google.maps.model.LatLng spotLatLng = new com.google.maps.model.LatLng(Double.parseDouble(spot.getLat()), Double.parseDouble(spot.getLng()));
                                LatLng key = new LatLng(Double.parseDouble(spot.getLat()), Double.parseDouble(spot.getLng()));
                                latLngs.add(key);

                                Log.d("count" , count + "");

                                waitCounter += 1;
                                new DistanceMatrixHelper().fetchDistanceToLocation(currentLatLng, spotLatLng, new DistanceResultReceiver(new Handler()), dialogFields, key);

                            }

                            changeMapFocus(latLngs);
                            }
                        }


                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, " onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                String lat = String.valueOf(marker.getPosition().latitude);
                                String lng = String.valueOf(marker.getPosition().longitude);
                                for (Map.Entry<LatLng, CustomDialogPayAndReserve> entry: dialogMap.entrySet()){
                                    LatLng latLng = entry.getKey();
                                    Dialog dialog = entry.getValue();
                                    String spotLat = String.valueOf(latLng.latitude);
                                    String spotLng = String.valueOf(latLng.longitude);
                                    if (spotLat.equals(lat) && spotLng.equals(lng)){
                                        Log.d("test", dialog.getWindow().toString());
                                        dialog.setCanceledOnTouchOutside(true);
                                        dialog.show();
                                        return true;
                                    }
                                }
                                return false;
                            }
                        });
                    }
                });
    }


    private String getLocationAddr(Spot spot) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        double lat = Double.parseDouble(spot.getLat());
        double lng = Double.parseDouble(spot.getLng());
        List<Address> listAddr = null;

        try{
            listAddr = geocoder.getFromLocation(lat, lng, 1);
        }
        catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

        if (listAddr == null || listAddr.size() == 0){
            Toast.makeText(context, "fail fetching geocode location data", Toast.LENGTH_SHORT).show();
            return "";
        }
        else{
            Address address = listAddr.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            return TextUtils.join(System.getProperty("line.separator"), addressFragments);
        }
    }

    class DistanceResultReceiver extends ResultReceiver {

        public DistanceResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData != null) {
                String distance = resultData.getString(DISTANCE);
                if (resultCode == DistanceMatrixHelper.SUCCESS && Double.parseDouble(distance.substring(0, distance.length() - 2)) < 100) {
                    MapActivityPresenter.this.waitCounter -= 1;
                    MapActivityPresenter.this.dialogMap.put((LatLng) resultData.get(KEY), ((MapsActivity)context).createCustomDialogPayAndReserve(resultData));

                    Log.d(this.getClass().getSimpleName(), resultData.getString(NAME) + "  " + distance);
                }
            }
        }
    }
}
