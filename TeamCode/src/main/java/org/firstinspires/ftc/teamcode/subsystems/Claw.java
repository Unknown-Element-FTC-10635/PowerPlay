package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Claw extends SubsystemBase {
    public enum State {
        OPEN,
        CLOSED
    }

    private final Telemetry telemetry;

    private final ServoEx leftRotation, rightRotation;

    private State currentState;

    public Claw(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftRotation = hardwareMap.get(ServoEx.class, "leftClaw");
        rightRotation = hardwareMap.get(ServoEx.class, "rightClaw");
        rightRotation.setInverted(true);

        currentState = State.CLOSED;
    }

    public void open() {
        leftRotation.turnToAngle(0.7);
        rightRotation.turnToAngle(0.7);

        currentState = State.OPEN;
    }

    public void close() {
        rightRotation.turnToAngle(0.1);
        leftRotation.turnToAngle(0.1);

        currentState = State.CLOSED;
    }

    public State getCurrentState() {
        return currentState;
    }
}
