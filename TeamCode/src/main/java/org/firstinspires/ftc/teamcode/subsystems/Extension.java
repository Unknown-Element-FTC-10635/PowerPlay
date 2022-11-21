package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Extension extends SubsystemBase {
    public enum ExtensionPosition {
        RETRACTED(0.1),
        SUBSTATION(0.8),
        TERMINAL(0.3),
        LOW(0.4),
        MEDIUM(0.6),
        HIGH(0.7);

        public double position;

        ExtensionPosition(double position) {
            this.position = position;
        }
    }

    private final Telemetry telemetry;

    private final Servo leftExtension, rightExtension;

    public Extension(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftExtension = hardwareMap.get(Servo.class, "leftExtension"); //hardwareMap.get(Motor.class, "leftExtension");
        rightExtension = hardwareMap.get(Servo.class, "rightExtension"); //hardwareMap.get(Motor.class, "rightExtension");
        rightExtension.setDirection(Servo.Direction.REVERSE);
    }

    public void goTo(ExtensionPosition position) {
        leftExtension.setPosition(position.position);
        rightExtension.setPosition(position.position);
    }

    @Override
    public void periodic() {
        telemetry.addData("Extension (Left) Position:", leftExtension.getPosition());
        telemetry.addData("Extension (Right) Position:", rightExtension.getPosition());
    }
}
