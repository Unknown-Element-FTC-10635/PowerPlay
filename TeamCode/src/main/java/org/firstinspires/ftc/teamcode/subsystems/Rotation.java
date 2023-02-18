package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;

public class Rotation extends SubsystemBase {
    private static final int MAX_ANGLE = 220;
    private static final double MULTIPLIER = 17;
    private static final double MINIMUM_SPEED = 2.2;

    private final Telemetry telemetry;
    private final DcMotorEx rotation;
    private double currentAngle = 0;
    private double targetPower = 0;

    private int targetAngle;

    private boolean goingUp = false;

    public Rotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        rotation = hardwareMap.get(DcMotorEx.class, "rotation"); //new Motor(hardwareMap, "rotation");
        rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void periodic() {
        currentAngle = Math.max(0, -rotation.getCurrentPosition() / 22.8);

        telemetry.addData("Rotation Current Angle", currentAngle);
        //telemetry.addData("Rotation Raw Value", -rotation.getCurrentPosition());

        if (CurrentOpmode.getCurrentOpmode() == CurrentOpmode.OpMode.AUTO) {
            telemetry.update();
        }
    }

    public void manualRotation(double power) {
        power = power * .7;

        rotation.setPower(power);

        /*if (power > 0 && currentAngle > MAX_ANGLE / 2.0) {
            rotation.setPower(power); //* calculateMulitpler(power));
        } else if (power < 0 && currentAngle < MAX_ANGLE / 2.0) {
            rotation.setPower(power); //* calculateMulitpler(power));
        } else {
            rotation.setPower(power);
        }*/

        telemetry.addData("Rotation Power", power);
        telemetry.addData("Rotation Multiplier", calculateMulitpler(power));
    }

    public void rotateTo(int targetAngle, double power) {
            rotation.setTargetPosition(targetAngle);

            goingUp = targetAngle > currentAngle;
            if (goingUp) {
                rotation.setPower(power);
            } else {
                rotation.setPower(-power);
            }

            this.targetAngle = targetAngle;
            this.targetPower = power;
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
        // https://www.desmos.com/calculator/xegbrro6fw
        double lower = Math.pow(Math.E, -((1.0 / MULTIPLIER) * (Math.abs(currentAngle)) - MINIMUM_SPEED));
        double upper = Math.pow(Math.E, -((1.0 / MULTIPLIER) * (MAX_ANGLE - Math.abs(currentAngle)) - MINIMUM_SPEED));
        return 1 / (1 + lower + upper);
    }

    public void stop() {
        rotation.setPower(0);
        targetPower = 0;
    }

    public void reset() {
        rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getAngle() {
        return currentAngle;
    }

    public boolean isGoingUp() {
        return goingUp;
    }
}
