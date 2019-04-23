package com.example.parkinggooglemapapp.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.parkinggooglemapapp.R;
import com.example.parkinggooglemapapp.presenter.CustomDialogPayAndReserve;
import com.example.parkinggooglemapapp.presenter.MapActivityPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private MapActivityPresenter presenter;
    private GoogleMap mMap;

    public CustomDialogPayAndReserve createCustomDialogPayAndReserve(Bundle resultData) {
        return new CustomDialogPayAndReserve(this, resultData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        presenter = new MapActivityPresenter(this, mMap);
        presenter.searchSurroundingParkingSpots(37.7749, -122.4194);
    }

}
