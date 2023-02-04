package org.firstinspires.ftc.teamcode.util.lift;

public enum ConeStackLevel implements LiftHeight {
    FIVE(340),
    FOUR(220),
    THREE(200),
    TWO(200),
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
