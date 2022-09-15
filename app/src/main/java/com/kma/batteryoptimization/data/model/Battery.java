package com.kma.batteryoptimization.data.model;

import androidx.annotation.NonNull;

public class Battery {
    private int healthStatus;

    private int level;

    private int scale;

    private int plugged;

    private boolean present;

    private int status;

    private String technology;

    private int temperature;

    private int voltage;

    public Battery() {
    }

    public Battery(int healthStatus, int level, int scale, int plugged, boolean present, int status, String technology, int temperature, int voltage) {
        this.healthStatus = healthStatus;
        this.level = level;
        this.scale = scale;
        this.plugged = plugged;
        this.present = present;
        this.status = status;
        this.technology = technology;
        this.temperature = temperature;
        this.voltage = voltage;
    }

    @NonNull
    @Override
    public String toString() {
        return "Battery{" +
            "healthStatus=" + healthStatus +
            ", level=" + level +
            ", scale=" + scale +
            ", plugged=" + plugged +
            ", present=" + present +
            ", status=" + status +
            ", technology='" + technology + '\'' +
            ", temperature=" + temperature +
            ", voltage=" + voltage +
            '}';
    }

    public int getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(int healthStatus) {
        this.healthStatus = healthStatus;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPlugged() {
        return plugged;
    }

    public void setPlugged(int plugged) {
        this.plugged = plugged;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }
}
