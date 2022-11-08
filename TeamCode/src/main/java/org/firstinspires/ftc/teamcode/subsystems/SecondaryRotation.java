package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SecondaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final Motor rotation;

    private double angle;

    public SecondaryRotation(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        rotation = new Motor(hardwareMap, "SRotation");
        rotation.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    public void rotatePower(float power) {
        rotation.setRunMode(Motor.RunMode.RawPower);

        rotation.set(power);
    }

    public void rotateTo(int targetAngle, double power) {
        rotation.setRunMode(Motor.RunMode.PositionControl);

        rotation.setTargetPosition(targetAngle);

        rotation.set(power);
    }


    public double getAngle() {
        return angle;
    }

    @Override
    public void periodic() {
        angle = rotation.getDistance();

        telemetry.addData("Secondary Rotation Angle:", angle);
    }
}
