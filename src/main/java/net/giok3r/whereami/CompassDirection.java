package net.giok3r.whereami;

public enum CompassDirection {
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW,
    N;

    public static CompassDirection fromAngle(float angle) {
        int adjustedAngle = Math.round(angle % 360f + 180f - 22.5f);
        if (adjustedAngle < 0) {
            adjustedAngle += 360;
        }
        if (adjustedAngle >= 360) {
            adjustedAngle -= 360;
        }
        int index = adjustedAngle * 8 / 360;
        return values()[index];
    }
}
