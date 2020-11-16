package com.benergy.flyperms.utils;

import com.benergy.flyperms.api.SpeedGroup;

public class SpeedRange implements SpeedGroup {
    private final String name;
    private final double lowerLimit;
    private final double upperLimit;

    public SpeedRange(String name, double lowerLimit, double upperLimit) {
        this.name = name.toLowerCase();
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public boolean isInRange(double speed) {
        return lowerLimit <= speed && upperLimit >= speed;
    }

    public String getName() {
        return name;
    }

    public String permission() {
        return "flyperms.speed." + this.name;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    @Override
    public String toString() {
        return "SpeedRange{" +
                "name='" + name + '\'' +
                ", lowerLimit=" + lowerLimit +
                ", upperLimit=" + upperLimit +
                '}';
    }
}
