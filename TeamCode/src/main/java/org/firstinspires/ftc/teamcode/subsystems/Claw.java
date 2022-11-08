package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Claw extends SubsystemBase {
    public enum State {
        OPEN,
        CLOSED
    }

    private final Telemetry telemetry;

    private final Servo leftRotation;
    private final ServoEx rightRotation;

    private State currentState;

    public Claw(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftRotation = hardwareMap.get(Servo.class, "leftClaw"); //new SimpleServo(hardwareMap, "leftClaw", 0, 180, AngleUnit.DEGREES);
        rightRotation = new SimpleServo(hardwareMap, "rightClaw", 0, 180, AngleUnit.DEGREES); //hardwareMap.get(ServoEx.class, "rightClaw");
        //rightRotation.setInverted(true);

        currentState = State.CLOSED;
    }

    public void open() {
        leftRotation.setPosition(0.95);
        rightRotation.setPosition(0.35);

        currentState = State.OPEN;
    }

    public void close() {
        leftRotation.setPosition(0.75);
        rightRotation.setPosition(0.55);

        currentState = State.CLOSED;
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void periodic() {
        telemetry.addData("Claw", currentState);

        telemetry.addData("Left Claw", leftRotation.getPosition());
        telemetry.addData("Right Claw", rightRotation.getPosition());

    }
}
