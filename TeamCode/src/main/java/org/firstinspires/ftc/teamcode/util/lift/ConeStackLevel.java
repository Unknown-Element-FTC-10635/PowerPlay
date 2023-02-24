package org.firstinspires.ftc.teamcode.util.lift;

public enum ConeStackLevel implements LiftHeight {
    BACK_AWAY(850),
    FIVE(385),
    FOUR(275),
    THREE(120),
    TWO(40),
    ONE(0);

    private final int height;

    ConeStackLevel(int height) {
        this.height = height;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
