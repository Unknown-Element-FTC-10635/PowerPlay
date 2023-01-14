package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;

public class Rotation extends SubsystemBase {
    private static final int MAX_ANGLE = 140;
    private final Telemetry telemetry;
    private final Motor rotation;
    private double currentAngle = 0;
    private double targetPower = 0;

    private int targetAngle;

    private boolean goingUp = false;
    private boolean isMoving = false;

    public Rotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        rotation = new Motor(hardwareMap, "rotation");
        rotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        rotation.resetEncoder();
    }

    @Override
    public void periodic() {
        currentAngle = Math.max(0, -rotation.encoder.getPosition() / 22.8);

        telemetry.addData("Rotation Current Angle", currentAngle);
        telemetry.addData("Rotation Target Angle", targetAngle);
        telemetry.addData("Rotation Raw Value", rotation.encoder.getPosition());

        if (CurrentOpmode.getCurrentOpmode() == CurrentOpmode.OpMode.AUTO) {
            //if (currentAngle <= MAX_ANGLE && targetPower > 0) {
                //rotation.set(targetPower * calculateMulitpler(targetPower));
            //}

            telemetry.update();
        }
    }

    public void manualRotation(double power) {
        rotation.setRunMode(Motor.RunMode.RawPower);

        power = power * 0.75;

        if (currentAngle <= MAX_ANGLE && power > 0) {
            rotation.set(power * calculateMulitpler(power));
        } else if (power < 0) {
            rotation.set(power);
        }


        isMoving = (power != 0);
        telemetry.addData("Rotation Power", power);
        telemetry.addData("Rotation Multiplier", calculateMulitpler(power));
        telemetry.addData("Rotation Moving", isMoving);
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
            this.targetPower = power;
            isMoving = true;
    }

    public boolean atTargetPosition() {
        // Positive Up; Negative Down
        if (goingUp) {
            return currentAngle >= targetAngle;
        } else {
            return currentAngle <= targetAngle;
        }
    }

    private double calculateMulitpler(double power) {
        // https://www.desmos.com/calculator/rfftz7qk4q
        return 1 / (1 + Math.pow(Math.E, -((1.0 / 4.5) * (MAX_ANGLE - Math.abs(currentAngle)) - 3)));
    }

    private void detectIfStuck() {
        if (isMoving) {

        }
    }

    public void stop() {
        rotation.set(0);
        rotation.stopMotor();
        targetPower = 0;
        isMoving = false;
    }

    public void reset() {
        rotation.resetEncoder();
        rotation.encoder.reset();
    }

    public double getAngle() {
        return currentAngle;
    }

    public boolean isGoingUp() {
        return goingUp;
    }
}
