package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Rotation extends SubsystemBase {
    private final DcMotor rotation;

    public Rotation(HardwareMap hardwareMap, Telemetry telemetry) {
        rotation = hardwareMap.get(DcMotor.class, "rotation");
        rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void manualRotation(double power) {
        rotation.setPower(power * 0.75);
    }
}
