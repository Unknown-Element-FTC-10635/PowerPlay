package org.firstinspires.ftc.teamcode.util;

public class CurrentOpmode {
    public enum OpMode {
        TELEOP,
        AUTO,
        UNKNOWN
    }

    private static OpMode currentOpmode = OpMode.UNKNOWN;

    public static OpMode getCurrentOpmode() {
        return currentOpmode;
    }

    public static void setCurrentOpmode(OpMode currentOpmode) {
        CurrentOpmode.currentOpmode = currentOpmode;
    }
}
