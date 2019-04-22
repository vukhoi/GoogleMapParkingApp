package com.example.parkinggooglemapapp.presenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.parkinggooglemapapp.R;

public class CustomDialogCheck extends Dialog implements View.OnClickListener {
    View view;

    public CustomDialogCheck(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.custsom_dialog_check_layout, null);
        setContentView(view);
        findViewById(R.id.btn_check).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                // todo start new activity into the detail
                break;
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
