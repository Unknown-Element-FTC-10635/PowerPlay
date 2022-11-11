package org.firstinspires.ftc.teamcode.util;

public enum RobotLocation {
    BLUE_LEFT(-1, 1, 2, 270),
    BLUE_RIGHT(1, 1, 1, 270),
    RED_LEFT(-1, -1, 1, 90),
    RED_RIGHT(1, -1, -1, 90);

    public final int x, y, start;
    public final double angle;

    RobotLocation(int x, int y, double angle, int start) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.start = start;
    }
}
