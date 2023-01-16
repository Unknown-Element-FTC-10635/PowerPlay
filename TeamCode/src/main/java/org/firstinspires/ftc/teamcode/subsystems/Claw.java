package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Claw extends SubsystemBase {
    public enum State {
        OPEN,
        CLOSED,
        UNKNOWN
    }

    private final Telemetry telemetry;

    private final Servo servo;

    private State currentState;

    public Claw(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        servo = hardwareMap.get(Servo.class, "claw"); //new SimpleServo(hardwareMap, "leftClaw", 0, 180, AngleUnit.DEGREES);

        currentState = State.UNKNOWN;
    }

    // https://www.desmos.com/calculator/p1p6mngfet
    public void openBig() {
        servo.setPosition(0.35);

        currentState = State.OPEN;
    }

    public void openSmall() {
        servo.setPosition(0.43);

        currentState = State.OPEN;
    }

    public void close() {
        servo.setPosition(0.49);

        currentState = State.CLOSED;
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void periodic() {
        telemetry.addData("Claw", currentState);

        telemetry.addData("Claw", servo.getPosition());
    }
}
