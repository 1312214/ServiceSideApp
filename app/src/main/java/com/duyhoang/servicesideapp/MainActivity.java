package com.duyhoang.servicesideapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStartService, btnStopService;

    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartService = (Button)findViewById(R.id.button_start);
        btnStopService = (Button)findViewById(R.id.button_stop);

        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        serviceIntent = new Intent(this, RandomNumberService.class);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_start:
                startService(serviceIntent);
                Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_stop: stopService(serviceIntent);
                break;
        }
    }
}
