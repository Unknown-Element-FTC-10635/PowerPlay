package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Extension extends SubsystemBase {
    private final Telemetry telemetry;

    private final Motor leftExtension, rightExtension;

    private double distance, targetInches;

    public Extension(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftExtension = new Motor(hardwareMap, "leftExtension"); //hardwareMap.get(Motor.class, "leftExtension");
        rightExtension = new Motor(hardwareMap, "rightExtension"); //hardwareMap.get(Motor.class, "rightExtension");
        rightExtension.setInverted(true);
    }

    public void rotatePower(float power) {
        leftExtension.setRunMode(Motor.RunMode.RawPower);
        rightExtension.setRunMode(Motor.RunMode.RawPower);

        leftExtension.set(power);
        rightExtension.set(power);
    }

    public void rotateInches(double inches, double power) {
        leftExtension.setRunMode(Motor.RunMode.PositionControl);
        rightExtension.setRunMode(Motor.RunMode.PositionControl);

        targetInches = inches / 7.0314986;
        leftExtension.setTargetDistance(targetInches);
        rightExtension.setTargetDistance(targetInches);

        leftExtension.set(power);
        rightExtension.set(power);
    }

    public void stop() {
        leftExtension.stopMotor();
        rightExtension.stopMotor();
    }

    public boolean atTargetExtension() {
        return (distance > targetInches);
    }

    public boolean atTargetRetraction() {
        return (distance < targetInches);
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public void periodic() {
        distance = (leftExtension.getDistance() + leftExtension.getDistance()) / 2;

        telemetry.addData("Secondary Rotation Distance:", distance / 7.0314986);
    }
}
