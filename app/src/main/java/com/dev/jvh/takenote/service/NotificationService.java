package com.dev.jvh.takenote.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JochemVanHespen on 12/2/2017.
 */

public class NotificationService extends Service {
    private ExecutorService executorService;
    @Override
    public void onCreate() {
        executorService = Executors.newSingleThreadExecutor();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //DO work for the service
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
