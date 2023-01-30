package org.firstinspires.ftc.teamcode.util.lift;

public enum ConeStackLevel implements LiftHeight {
    FIVE(600),
    FOUR(50),
    THREE(30),
    TWO(10),
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
