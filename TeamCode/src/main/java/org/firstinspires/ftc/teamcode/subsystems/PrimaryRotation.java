package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PrimaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final Motor leftRotation, rightRotation;
    private final Motor.Encoder leftBarEncoder;

    private int angle;

    public PrimaryRotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftRotation = hardwareMap.get(Motor.class, "leftPRotation");
        rightRotation = hardwareMap.get(Motor.class, "rightPRotation");
        leftBarEncoder = hardwareMap.get(Motor.Encoder.class, "br");

        leftRotation.encoder = leftBarEncoder;
        rightRotation.encoder = leftBarEncoder;

        leftRotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightRotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightRotation.setInverted(true);
    }

    public void rotatePower(float power) {
        leftRotation.setRunMode(Motor.RunMode.RawPower);
        rightRotation.setRunMode(Motor.RunMode.RawPower);

        leftRotation.set(power);
        rightRotation.set(power);
    }

    public void rotateTo(int targetAngle, double power) {
        leftRotation.setRunMode(Motor.RunMode.PositionControl);
        rightRotation.setRunMode(Motor.RunMode.PositionControl);

        leftRotation.setTargetPosition(targetAngle * 256);
        rightRotation.setTargetPosition(targetAngle * 256);

        leftRotation.set(power);
        rightRotation.set(power);
    }

    public boolean atTargetPosition() {
        return (leftRotation.atTargetPosition() && rightRotation.atTargetPosition());
    }

    public void stop() {
        leftRotation.stopMotor();
        rightRotation.stopMotor();
    }

    public void reset() {
        leftRotation.resetEncoder();
        rightRotation.resetEncoder();
    }

    public int getAngle() {
        return angle;
    }

    @Override
    public void periodic() {
        angle = leftBarEncoder.getPosition() / 256;

        telemetry.addData("Primary Rotation Angle:", angle);
    }
}
