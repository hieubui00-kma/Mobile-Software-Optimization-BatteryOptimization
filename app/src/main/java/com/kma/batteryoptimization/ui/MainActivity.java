package com.kma.batteryoptimization.ui;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static com.kma.batteryoptimization.util.Battery.getBattery;
import static com.kma.batteryoptimization.util.Battery.getBatteryHealthStatus;
import static com.kma.batteryoptimization.util.Battery.getBatteryPlugged;
import static com.kma.batteryoptimization.util.Battery.getBatteryStatus;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kma.batteryoptimization.R;
import com.kma.batteryoptimization.data.model.Battery;
import com.kma.batteryoptimization.data.receiver.BatteryReceiver;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvBatteryProfile;

    private BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBatteryProfile = findViewById(R.id.tv_battery_profile);
        batteryReceiver = new BatteryReceiver();

        batteryReceiver.getBattery().observe(this, battery -> {
            String batteryProfile = getBatteryProfile(battery);

            tvBatteryProfile.setText(batteryProfile);
        });

        getCurrentBatteryProfile();
    }

    @NonNull
    private String getBatteryProfile(@NonNull Battery battery) {
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

    private void getCurrentBatteryProfile() {
        Intent intent = registerReceiver(null, new IntentFilter(ACTION_BATTERY_CHANGED));

        if (intent == null) {
            return;
        }

        final Battery battery = getBattery(intent);
        String batteryProfile = getBatteryProfile(battery);

        tvBatteryProfile.setText(batteryProfile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(batteryReceiver, new IntentFilter(ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryReceiver);
        super.onPause();
    }
}