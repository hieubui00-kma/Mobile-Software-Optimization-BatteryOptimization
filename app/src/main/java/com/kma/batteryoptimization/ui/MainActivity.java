package com.kma.batteryoptimization.ui;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.kma.batteryoptimization.util.Battery.getBattery;
import static com.kma.batteryoptimization.util.Battery.getBatteryHealthStatus;
import static com.kma.batteryoptimization.util.Battery.getBatteryPlugged;
import static com.kma.batteryoptimization.util.Battery.getBatteryStatus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kma.batteryoptimization.R;
import com.kma.batteryoptimization.data.model.Battery;
import com.kma.batteryoptimization.data.receiver.BatteryReceiver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvBatteryProfile;

    private TextView tvNetworkProfile;

    private TextView tvLocation;

    private BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBatteryProfile = findViewById(R.id.tv_battery_profile);
        tvNetworkProfile = findViewById(R.id.tv_network_profile);
        tvLocation = findViewById(R.id.tv_location);
        batteryReceiver = new BatteryReceiver();

        batteryReceiver.getBattery().observe(this, battery -> {
            String batteryProfile = getBatteryProfile(battery);

            tvBatteryProfile.setText(batteryProfile);
        });

        getCurrentBatteryProfile();
        getCurrentNetworkProfile();
        requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 1000);
    }

    private String getBatteryProfile(Battery battery) {
        if (battery == null) {
            return null;
        }

        float percentage = battery.getScale() == 0 ? 0.0f : 100 * (battery.getLevel() / (float) battery.getScale());
        String level = String.format(Locale.getDefault(), "%d/%d (%.2f%%)", battery.getLevel(), battery.getScale(), percentage);

        return "Health: " + getBatteryHealthStatus(battery.getHealthStatus()) + "\n" +
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

    private void getCurrentNetworkProfile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = connectivityManager.getAllNetworks();
        StringBuilder networkProfileBuilder = new StringBuilder();
        NetworkInfo networkInfo;
        String networkProfile;

        for (Network network : networks) {
            networkInfo = connectivityManager.getNetworkInfo(network);
            networkProfileBuilder.append("Type: ").append(networkInfo.getTypeName()).append("\n")
                .append("\t\t- State: ").append(networkInfo.getState()).append("\n")
                .append("\t\t- Available: ").append(networkInfo.isAvailable()).append("\n")
                .append("\t\t- Roaming: ").append(networkInfo.isRoaming()).append("\n")
                .append("\n");
        }
        networkProfile = networkProfileBuilder.toString();
        tvNetworkProfile.setText(networkProfile.isEmpty() ? null : networkProfile.trim());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(batteryReceiver, new IntentFilter(ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
            requestCode == 1000
            && grantResults.length > 0
            && Arrays.stream(grantResults).allMatch(result -> result == PERMISSION_GRANTED)
        ) {
            requestLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getAllProviders();

        if (providers == null || providers.isEmpty()) {
            return;
        }

        LocationListener locationListener = new LocationListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("MainActivity", location.toString());
                tvLocation.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
                Log.d("MainActivity", provider + " change status to " + status + ".");
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
                Log.d("MainActivity", provider + " is enabled.");
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
                Log.d("MainActivity", provider + " is disabled.");
            }
        };

        providers.forEach(provider -> {
            Log.d("MainActivity", "Requesting location updates on " + provider);
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        });
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryReceiver);
        super.onPause();
    }
}