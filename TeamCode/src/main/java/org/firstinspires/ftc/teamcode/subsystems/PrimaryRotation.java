package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.SubsystemState;

public class PrimaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final Motor leftRotation, rightRotation;
    private final Motor.Encoder leftBarEncoder;

    private SubsystemState state = SubsystemState.UNKNOWN;

    private double angle, targetAngle, lockAngle, setPower;

    private boolean slowLift = false;

    private final int MAX_ANGLE = 171;

    public PrimaryRotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftRotation = new Motor(hardwareMap, "leftPRotation");//hardwareMap.get(Motor.class, "leftPRotation");
        rightRotation = new Motor(hardwareMap, "SRotation");
        leftBarEncoder = new Motor(hardwareMap, "fl").encoder;//hardwareMap.get(Motor.Encoder.class, "br");
        leftBarEncoder.reset();

        leftRotation.encoder = leftBarEncoder;
        leftRotation.resetEncoder();
        rightRotation.encoder = leftBarEncoder;
        rightRotation.resetEncoder();

        leftRotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightRotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightRotation.setInverted(true);
    }

    public void rotatePower(float power) {
        leftRotation.setRunMode(Motor.RunMode.RawPower);
        //rightRotation.setRunMode(Motor.RunMode.RawPower);

        if (angle < MAX_ANGLE) {
            // https://www.desmos.com/calculator/w8omojaitz
            double multiplier = 1/(1+Math.pow(Math.E, -((1.0/3.0) * (MAX_ANGLE - angle) - 3)));
            leftRotation.set(power * multiplier);
            rightRotation.set(power * multiplier);
            telemetry.addData("Primary Power", power * multiplier);
        } else if (power < 0){
            leftRotation.set(power);
            telemetry.addData("Primary Power", power);
        }
    }

    public void rotateTo(int targetAngle, double power) {
        leftRotation.setRunMode(Motor.RunMode.PositionControl);
        rightRotation.setRunMode(Motor.RunMode.PositionControl);

        leftRotation.setTargetPosition(targetAngle);
        rightRotation.setTargetPosition(targetAngle);

        setPower = power;
        leftRotation.set(setPower);
        rightRotation.set(setPower);

        this.targetAngle = targetAngle;
        telemetry.addData("Primary Power", power);

    }

    @Override
    public void periodic() {
        angle = leftBarEncoder.getPosition()/22.8;

        if (slowLift) {
            if (angle < MAX_ANGLE) {
                // https://www.desmos.com/calculator/w8omojaitz
                double multiplier = 1/(1+Math.pow(Math.E, -((1.0/3.0) * (MAX_ANGLE - angle) - 3)));
                leftRotation.set(setPower * multiplier);
                rightRotation.set(setPower * multiplier);
            } else if (setPower < 0){
                leftRotation.set(setPower);
            }
        }

        if (state == SubsystemState.STOPPING) {
            lockAngle = angle;
        } else if (state == SubsystemState.STOPPED && ((lockAngle - angle) * 100.0) / angle < 0.02) {
            //rotateTo(lockAngle, 0.025);
        }

        telemetry.addData("Primary State", state);
        telemetry.addData("Primary Rotation Angle", angle);
        telemetry.addData("Primary Rotation Raw Value", leftBarEncoder.getPosition());
    }

    public boolean atTargetPosition() {
        return ((targetAngle > 0 && angle > targetAngle) || (targetAngle < 0 && angle < targetAngle));
        // && rightRotation.atTargetPosition());
    }

    public void stop() {
        leftRotation.stopMotor();
        rightRotation.stopMotor();
    }

    public void reset() {
        leftRotation.resetEncoder();
        leftBarEncoder.reset();
        //angle = 45.6;
        //rightRotation.resetEncoder();
    }

    public double getAngle() {
        return angle;
    }

    public SubsystemState getState() {
        return state;
    }

    public void setState(SubsystemState state) {
        this.state = state;
    }

    public void setSlowLift(boolean slowLift) {
        this.slowLift = slowLift;
    }
}
