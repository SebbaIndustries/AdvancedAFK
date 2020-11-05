package com.sebbaindustries.advancedafk.engine.buffer.components;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class LocationPoint {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public LocationPoint(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
