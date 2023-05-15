package org.firstinspires.ftc.teamcode.util.lift;

public enum PoleLevel implements LiftHeight {
    SUBSTATION(0),
    LOW(100),
    MEDIUM(625),
    HIGH(1450),
    EXTRA_TALL(1600);

    private final int height;

    PoleLevel(final int height) {
        this.height = height;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public static LiftHeight getClosestHeight(LiftHeight height) {
        double smallestDistance = 1000;
        LiftHeight closest = SUBSTATION;

        for (LiftHeight enHeight : PoleLevel.values()) {
            double distance = Math.abs(enHeight.getHeight() - height.getHeight());
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closest = enHeight;
            }
        }

        return closest;
    }
}
