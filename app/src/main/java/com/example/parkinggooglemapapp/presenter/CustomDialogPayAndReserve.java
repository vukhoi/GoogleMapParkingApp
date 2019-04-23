package com.example.parkinggooglemapapp.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parkinggooglemapapp.R;
import com.example.parkinggooglemapapp.view.SpotDetailActivity;

public class CustomDialogPayAndReserve extends Dialog implements View.OnClickListener {
    private Context context;
    private Bundle resultData;
    private TextView tvName, tvAddress, tvDistance, tvCost, tvMore;
    private View view;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.custom_dialog_reserve_layout, null);

        setContentView(view);
        tvName = view.findViewById(R.id.tv_name);
        tvAddress = view.findViewById(R.id.tv_address);
        tvDistance = view.findViewById(R.id.tv_num_distance);
        tvCost = view.findViewById(R.id.tv_num_cost);
        tvMore = view.findViewById(R.id.tv_more);
        btn = view.findViewById(R.id.btn_pay_and_reserve);

        tvName.setText(resultData.getString(MapActivityPresenter.NAME));
        tvAddress.setText(resultData.getString(MapActivityPresenter.ADDR));
        tvCost.setText(resultData.getString(MapActivityPresenter.COST));
        tvDistance.setText(resultData.getString(MapActivityPresenter.DISTANCE));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDialogCheck(context);
            }
        });

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SpotDetailActivity.class);
                intent.putExtras(resultData);
                context.startActivity(intent);
            }
        });
    }

    public CustomDialogPayAndReserve(final Context context, final Bundle resultData) {
        super(context);
        this.context = context;
        this.resultData = resultData;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_and_reserve:
                new CustomDialogCheck(context);
                break;
            case R.id.tv_more:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
