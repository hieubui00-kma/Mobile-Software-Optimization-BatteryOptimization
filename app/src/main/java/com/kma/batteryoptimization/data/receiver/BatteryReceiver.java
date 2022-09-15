package com.kma.batteryoptimization.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kma.batteryoptimization.data.model.Battery;

public class BatteryReceiver extends BroadcastReceiver {
    private final MutableLiveData<Battery> battery = new MutableLiveData<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        final Battery battery = com.kma.batteryoptimization.util.Battery.getBattery(intent);

        this.battery.postValue(battery);
    }

    public LiveData<Battery> getBattery() {
        return battery;
    }
}
