package com.example.parkinggooglemapapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.parkinggooglemapapp.R;

public class SpotDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        View view = getLayoutInflater().inflate(R.layout.custom_dialog_reserve_layout, null);
        LinearLayout detailPlaceHolder = findViewById(R.id.alert_dialog_layout_placeholder);
        detailPlaceHolder.addView(view);
    }
}
