package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TertiaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final PrimaryRotation primaryRotation;

    private final ServoEx leftRotation, rightRotation;

    boolean beginAdjustment = false;

    public TertiaryRotation(HardwareMap hardwareMap, Telemetry telemetry, PrimaryRotation primaryRotation) {
        this.telemetry = telemetry;
        this.primaryRotation = primaryRotation;

        leftRotation = hardwareMap.get(ServoEx.class, "leftTRotation");
        rightRotation = hardwareMap.get(ServoEx.class, "rightTRotation");
        rightRotation.setInverted(true);
    }

    public void goToInitialPosition() {
        leftRotation.setPosition(0.25);
        rightRotation.setPosition(0.25);

        beginAdjustment = true;
    }

    public boolean isBeginningAdjustment() {
        return beginAdjustment;
    }

    @Override
    public void periodic() {
        if (beginAdjustment) {
            leftRotation.turnToAngle(leftRotation.getAngle() - primaryRotation.getAngle());
            rightRotation.turnToAngle(rightRotation.getAngle() - primaryRotation.getAngle());
        }

        telemetry.addData("Tertiary Rotation Angle:", leftRotation.getAngle());
    }
}
