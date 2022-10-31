package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SecondaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final ServoEx leftRotation, rightRotation;

    private double angle;

    public SecondaryRotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftRotation = hardwareMap.get(ServoEx.class, "leftSRotation");
        rightRotation = hardwareMap.get(ServoEx.class, "rightSRotation");
        rightRotation.setInverted(true);
    }

    public void rotatePower(float power) {
        leftRotation.setPosition(leftRotation.getPosition() + power * 0.01);
        rightRotation.setPosition(rightRotation.getPosition() + power * 0.01);
    }

    public void rotateTo(double angle) {
        leftRotation.setPosition(angle);
        rightRotation.setPosition(angle);
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public void periodic() {
        angle = (leftRotation.getAngle() + rightRotation.getAngle()) / 2;

        telemetry.addData("Secondary Rotation Angle:", angle);
    }
}
