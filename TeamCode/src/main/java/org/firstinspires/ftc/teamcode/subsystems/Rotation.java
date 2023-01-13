package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Rotation extends SubsystemBase {
    private static final int MAX_ANGLE = 155;
    private final Telemetry telemetry;
    private final Motor rotation;
    private double currentAngle = 0;
    private int targetAngle;
    private boolean goingUp = false;
    private double targetPower = 0;

    public Rotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        rotation = new Motor(hardwareMap, "rotation");
        rotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        rotation.resetEncoder();
    }

    //:)
    @Override
    public void periodic() {
        currentAngle = -rotation.encoder.getPosition() / 22.8;

        telemetry.addData("Rotation Current Angle", currentAngle);
        telemetry.addData("Rotation Target Angle", targetAngle);
        telemetry.addData("Rotation Raw Value", rotation.encoder.getPosition());
        telemetry.update();
    }

    public void manualRotation(double power) {
        rotation.setRunMode(Motor.RunMode.RawPower);
        power = power * 0.75;
        if (currentAngle <= MAX_ANGLE && power > 0) {
            // https://www.desmos.com/calculator/9isroywz7a
            double multiplier = 1 / (1 + Math.pow(Math.E, -((1.0 / 4.5) * (MAX_ANGLE - Math.abs(currentAngle)) - 2)));
            rotation.set(power * multiplier);

            telemetry.addData("Primary Power", power * multiplier);
        } else {
            rotation.set(power);

            telemetry.addData("Primary Power", power);
        }
    }

    public void rotateTo(int targetAngle, double power) {
        rotation.setRunMode(Motor.RunMode.PositionControl);

        rotation.setTargetPosition(targetAngle);

        goingUp = targetAngle > currentAngle;
        if (goingUp) {
            rotation.set(power);
        } else {
            rotation.set(-power);
        }

        this.targetAngle = targetAngle;
    }

    public boolean atTargetPosition() {
        // Positive Up; Negative Down
        if (goingUp) {
            return currentAngle >= targetAngle;
        } else {
            return currentAngle <= targetAngle;
        }
    }

    public void stop() {
        rotation.stopMotor();
        targetPower = 0;
    }

    public void reset() {
        rotation.resetEncoder();
        rotation.encoder.reset();
    }

    public double getAngle() {
        return currentAngle;
    }
}
