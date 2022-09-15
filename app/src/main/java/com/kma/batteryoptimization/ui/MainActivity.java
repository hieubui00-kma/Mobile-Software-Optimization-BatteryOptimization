package com.kma.batteryoptimization.ui;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;
import static android.os.BatteryManager.EXTRA_HEALTH;
import static android.os.BatteryManager.EXTRA_LEVEL;
import static android.os.BatteryManager.EXTRA_PLUGGED;
import static android.os.BatteryManager.EXTRA_PRESENT;
import static android.os.BatteryManager.EXTRA_SCALE;
import static android.os.BatteryManager.EXTRA_STATUS;
import static android.os.BatteryManager.EXTRA_TECHNOLOGY;
import static android.os.BatteryManager.EXTRA_TEMPERATURE;
import static android.os.BatteryManager.EXTRA_VOLTAGE;
import static com.kma.batteryoptimization.util.Battery.getBatteryHealthStatus;
import static com.kma.batteryoptimization.util.Battery.getBatteryPlugged;
import static com.kma.batteryoptimization.util.Battery.getBatteryStatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kma.batteryoptimization.R;
import com.kma.batteryoptimization.data.model.Battery;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvBatteryProfile;

    private BroadcastReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBatteryProfile = findViewById(R.id.tv_battery_profile);
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tvBatteryProfile.setText(getBatteryProfile(intent));
            }
        };
        Intent intent = registerReceiver(batteryReceiver, new IntentFilter(ACTION_BATTERY_CHANGED));

        tvBatteryProfile.setText(getBatteryProfile(intent));
    }

    private String getBatteryProfile(Intent intent) {
        if (intent == null) {
            return null;
        }

        Battery battery = getBattery(intent);
        float percentage = battery.getScale() == 0 ? 0.0f : 100 * (battery.getLevel() / (float) battery.getScale());
        String level = String.format(Locale.getDefault(), "%d/%d (%.2f%%)", battery.getLevel(), battery.getScale(), percentage);

        return "Battery Profile:\n" +
            "Health: " + getBatteryHealthStatus(battery.getHealthStatus()) + "\n" +
            "Level: " + level + "\n" +
            "Power source: " + getBatteryPlugged(battery.getPlugged()) + "\n" +
            "Present: " + (battery.isPresent() ? "Yes" : "No") + "\n" +
            "Status: " + getBatteryStatus(battery.getStatus()) + "\n" +
            "Technology: " + battery.getTechnology() + "\n" +
            "Temperature: " + battery.getTemperature() + "\n" +
            "Voltage: " + battery.getVoltage();
    }

    private Battery getBattery(Intent intent) {
        if (intent == null) {
            return null;
        }

        final Battery battery = new Battery();

        battery.setHealthStatus(intent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN));
        battery.setLevel(intent.getIntExtra(EXTRA_LEVEL, 0));
        battery.setScale(intent.getIntExtra(EXTRA_SCALE, 100));
        battery.setPlugged(intent.getIntExtra(EXTRA_PLUGGED, 0));
        battery.setPresent(intent.getBooleanExtra(EXTRA_PRESENT, false));
        battery.setStatus(intent.getIntExtra(EXTRA_STATUS, BATTERY_STATUS_UNKNOWN));
        battery.setTechnology(intent.getStringExtra(EXTRA_TECHNOLOGY));
        battery.setTemperature(intent.getIntExtra(EXTRA_TEMPERATURE, Integer.MIN_VALUE));
        battery.setVoltage(intent.getIntExtra(EXTRA_VOLTAGE, Integer.MIN_VALUE));
        return battery;
    }
}