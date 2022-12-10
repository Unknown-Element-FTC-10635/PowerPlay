package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class TertiaryRotation extends SubsystemBase {
    private final Telemetry telemetry;

    private final PrimaryRotation primaryRotation;

    private final ServoEx leftRotation, rightRotation;

    private boolean beginAdjustment = false;
    private double previousAngle;

    public TertiaryRotation(HardwareMap hardwareMap, Telemetry telemetry, PrimaryRotation primaryRotation) {
        this.telemetry = telemetry;
        this.primaryRotation = primaryRotation;

        leftRotation = new SimpleServo(hardwareMap, "leftTRotation", 0, 200, AngleUnit.DEGREES);//hardwareMap.get(ServoEx.class, "leftTRotation");
        rightRotation = new SimpleServo(hardwareMap, "rightTRotation", 0, 200, AngleUnit.DEGREES); //hardwareMap.get(ServoEx.class, "rightTRotation");
        rightRotation.setInverted(true);
    }

    public void goToInitialPosition() {
        leftRotation.setPosition(0.60);
        rightRotation.setPosition(0.60);
    }

    public void increasePositiveDirection() {
        leftRotation.setPosition(leftRotation.getPosition() + 0.01);
        rightRotation.setPosition(rightRotation.getPosition() + 0.01);
    }

    public void increaseNegativeDirection() {
        leftRotation.setPosition(leftRotation.getPosition() - 0.01);
        rightRotation.setPosition(rightRotation.getPosition() - 0.01);
    }

    public void goToPosition(double position) {
        leftRotation.setPosition(position);
        rightRotation.setPosition(position);
    }

    public boolean isBeginningAdjustment() {
        return beginAdjustment;
    }

    public void setBeginAdjustment(boolean beginAdjustment) {
        this.beginAdjustment = beginAdjustment;
    }

    @Override
    public void periodic() {
        if (beginAdjustment) {
            double primaryAngle = primaryRotation.getAngle();

            leftRotation.setPosition(0.34 + (0.35/90) * (180-primaryAngle));
            rightRotation.setPosition(0.34 + (0.35/90) * (180-primaryAngle));

        }

        telemetry.addData("Tertiary Rotation (left) Angle:", leftRotation.getPosition());
        telemetry.addData("Tertiary Rotation (right) Angle:", rightRotation.getPosition());

        telemetry.addData("Adjustment Value", 0.34 + (0.35/90) * (180-primaryRotation.getAngle()));
    }
}
