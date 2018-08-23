package com.duyhoang.servicesideapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by rogerh on 5/29/2018.
 */

public class RandomNumberService extends Service {

    private static String TAG = RandomNumberService.class.getSimpleName();
    private final int GET_RANDOM_NUMBER_CODE = 1;
    private final int MAX = 100;
    private final int MIN = 0;
    private final String ACCEPTED_PACKAGE = "com.duyhoang.clientsideappusingrandomnumberservice";

    boolean isRunning;
    private int randomNumberValue;
    Messenger randomNumberMessenger = new Messenger(new RandomNumberRequestedHandler());


    class RandomNumberRequestedHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_RANDOM_NUMBER_CODE:
                    sendRandomNumberToRequester();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void sendRandomNumberToRequester() {
        Message returnedMsg = Message.obtain(null, GET_RANDOM_NUMBER_CODE);
        returnedMsg.arg1 = getRandomNumberValue();
        try {
            returnedMsg.replyTo.send(returnedMsg);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberMachine();
            }
        }).start();

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "in onBind method");
        if (intent.getPackage().equals(ACCEPTED_PACKAGE)) {
            Toast.makeText(getApplicationContext(), "Received correct package", Toast.LENGTH_SHORT).show();
            return randomNumberMessenger.getBinder();
        } else {
            Toast.makeText(getApplicationContext(), "Received Uncorrect package", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberMachine();
    }

    public int getRandomNumberValue() {
        return randomNumberValue;
    }

    private void startRandomNumberMachine() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
            randomNumberValue = new Random().nextInt(MAX) + MIN;
            Log.i(TAG, "Inside onStartCommand - Thread id: " + Thread.currentThread().getId() + " Random Number: " + randomNumberValue);
        }
    }

    private void stopRandomNumberMachine() {
        isRunning = false;
        Toast.makeText(getApplicationContext(), "Service Stoped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
