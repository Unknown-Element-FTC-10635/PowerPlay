package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.SubsystemState;

public class PrimaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final Motor leftRotation;//, rightRotation;
    private final Motor.Encoder leftBarEncoder;

    private SubsystemState state = SubsystemState.UNKNOWN;

    private double angle, lockAngle;

    public PrimaryRotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftRotation = new Motor(hardwareMap, "leftPRotation");//hardwareMap.get(Motor.class, "leftPRotation");
        //rightRotation = new Motor(hardwareMap, "rightPRotation"); //hardwareMap.get(Motor.class, "rightPRotation");
        leftBarEncoder = new Motor(hardwareMap, "fl").encoder;//hardwareMap.get(Motor.Encoder.class, "br");
        leftBarEncoder.reset();

        leftRotation.encoder = leftBarEncoder;
        //rightRotation.encoder = leftBarEncoder;

        leftRotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        //rightRotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        //rightRotation.setInverted(true);
    }

    public void rotatePower(float power) {
        leftRotation.setRunMode(Motor.RunMode.RawPower);
        //rightRotation.setRunMode(Motor.RunMode.RawPower);

        if (angle < 170) {
            // https://www.desmos.com/calculator/w8omojaitz
            double multiplier = 1/(1+Math.pow(Math.E, -((1.0/3.0) * (170 - angle) - 3)));
            leftRotation.set(power * multiplier);
        } else if (power < 0){
            leftRotation.set(power);
        }
        //rightRotation.set(power);
    }

    public void rotateTo(int targetAngle, double power) {
        leftRotation.setRunMode(Motor.RunMode.PositionControl);
        //rightRotation.setRunMode(Motor.RunMode.PositionControl);

        leftRotation.setTargetPosition(targetAngle);
        //rightRotation.setTargetPosition(targetAngle);

        leftRotation.set(power);
        //rightRotation.set(power);
    }

    @Override
    public void periodic() {
        angle = leftBarEncoder.getPosition()* 0.17578*0.25+45.6;

        if (state == SubsystemState.STOPPING) {
            lockAngle = angle;
        } else if (state == SubsystemState.STOPPED && ((lockAngle - angle) * 100.0) / angle < 0.02) {
            //rotateTo(lockAngle, 0.025);
        }

        telemetry.addData("Primary Rotation Angle:", angle);
        telemetry.addData("Primary State", state);
    }

    public boolean atTargetPosition() {
        return (leftRotation.atTargetPosition()); // && rightRotation.atTargetPosition());
    }

    public void stop() {
        leftRotation.stopMotor();
        //rightRotation.stopMotor();
    }

    public void reset() {
        leftRotation.resetEncoder();
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
}
