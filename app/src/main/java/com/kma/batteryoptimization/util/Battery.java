package com.kma.batteryoptimization.util;

import static android.os.BatteryManager.BATTERY_HEALTH_DEAD;
import static android.os.BatteryManager.BATTERY_HEALTH_GOOD;
import static android.os.BatteryManager.BATTERY_HEALTH_OVERHEAT;
import static android.os.BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE;
import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE;
import static android.os.BatteryManager.BATTERY_PLUGGED_AC;
import static android.os.BatteryManager.BATTERY_PLUGGED_USB;
import static android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS;
import static android.os.BatteryManager.BATTERY_STATUS_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_DISCHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;
import static android.os.BatteryManager.BATTERY_STATUS_NOT_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Battery {

    @NonNull
    @Contract(pure = true)
    public static String getBatteryHealthStatus(int healthStatus) {
        switch (healthStatus) {
            case BATTERY_HEALTH_GOOD:
                return "Good";

            case BATTERY_HEALTH_OVERHEAT:
                return "Overheat";

            case BATTERY_HEALTH_DEAD:
                return "Dead";

            case BATTERY_HEALTH_OVER_VOLTAGE:
                return "Over voltage";

            case BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return "Unspecified failure";

            case BATTERY_HEALTH_UNKNOWN:
            default:
                return "Unknown";
        }
    }

    @NonNull
    @Contract(pure = true)
    public static String getBatteryStatus(int status) {
        switch (status) {
            case BATTERY_STATUS_CHARGING:
                return "Charging";

            case BATTERY_STATUS_DISCHARGING:
                return "Discharging";

            case BATTERY_STATUS_NOT_CHARGING:
                return "Not charging";

            case BATTERY_STATUS_FULL:
                return "Full";

            case BATTERY_STATUS_UNKNOWN:
            default:
                return "Unknown";
        }
    }

    @NonNull
    @Contract(pure = true)
    public static String getBatteryPlugged(int plugged) {
        switch (plugged) {
            case BATTERY_PLUGGED_AC:
                return "AC";

            case BATTERY_PLUGGED_USB:
                return "USB";

            case BATTERY_PLUGGED_WIRELESS:
                return "Wireless";

            default:
                return "Unknown";
        }
    }
}
