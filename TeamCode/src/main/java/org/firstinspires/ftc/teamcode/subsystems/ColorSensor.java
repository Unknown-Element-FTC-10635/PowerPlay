package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor extends SubsystemBase {
    private final Telemetry telemetry;

    private final RevColorSensorV3 left, right;

    public ColorSensor(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        left = hardwareMap.get(RevColorSensorV3.class, "leftColor");
        right = hardwareMap.get(RevColorSensorV3.class, "rightColor");
    }

    @Override
    public void periodic() {
        telemetry.addData("Left Distance Sensor (red/blue)", Math.max(left.red(), left.blue()));
        telemetry.addData("Left Distance Sensor (green)", left.green());
        telemetry.addData("Right Distance Sensor (red/blue)", Math.max(right.red(), right.blue()));
        telemetry.addData("Right Distance Sensor (green)", right.green());
    }

    public boolean leftNotGreen() {
        double coneColor = Math.max(left.red(), left.blue());
        return coneColor > left.green();
    }

    public boolean rightNotGreen() {
        double coneColor = Math.max(right.red(), right.blue());
        return coneColor > right.green();
    }
}
