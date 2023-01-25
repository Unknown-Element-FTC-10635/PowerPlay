package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;
import org.firstinspires.ftc.teamcode.util.color.RGB;
import org.firstinspires.ftc.teamcode.util.color.TapeMeasureColor;

public class Extension extends SubsystemBase {
    private final Telemetry telemetry;

    private final Motor leftExtension, rightExtension;
    private final ColorSensor tapeMeasure;

    private double currentPos;

    private static final double P = 0, I = 0, D = 0
    private static final double F = 0;
    private static final double TICKS_PER_DEGREE = 537.7 / 360;

    private PIDFController pidfController;

    private TapeMeasureColor currentLevel, targetLevel = TapeMeasureColor.UNKNOWN;
    private RGB currentColor;

    public Extension(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftExtension = new Motor(hardwareMap, "leftExtension"); //hardwareMap.get(Motor.class, "leftExtension");
        rightExtension = new Motor(hardwareMap, "rightExtension"); //hardwareMap.get(Motor.class, "rightExtension");
        rightExtension.setInverted(true);

        leftExtension.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightExtension.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        tapeMeasure = hardwareMap.get(RevColorSensorV3.class, "tapeMeasure");

        pidfController.setSetPoint(0);
    }

    public void rotatePower(float power) {
        leftExtension.setRunMode(Motor.RunMode.RawPower);
        rightExtension.setRunMode(Motor.RunMode.RawPower);

        leftExtension.set(power);
        rightExtension.set(power);
    }

    public void rotateLevel(TapeMeasureColor targetColor, double power) {
        leftExtension.setRunMode(Motor.RunMode.RawPower);
        rightExtension.setRunMode(Motor.RunMode.RawPower);

        targetLevel = targetColor;

        if (targetColor.getOrder() > currentLevel.getOrder()) {
            leftExtension.set(power);
            rightExtension.set(power);
        } else if (targetColor.getOrder() < currentLevel.getOrder()) {
            leftExtension.set(-power);
            rightExtension.set(-power);
        }
    }

    @Override
    public void periodic() {
        currentPos = (leftExtension.getDistance() + leftExtension.getDistance()) / 2;

        currentColor = new RGB(tapeMeasure.red(), tapeMeasure.green(), tapeMeasure.blue());
        currentLevel = getCurrentLevel();

        if (CurrentOpmode.getCurrentOpmode() == CurrentOpmode.OpMode.AUTO) {
            if (!atTargetLevel() && targetLevel != TapeMeasureColor.UNKNOWN) {
                rotateLevel(targetLevel, Math.abs(pidfController.calculate(currentPos)));
            }
        }

        telemetry.addData("Rotation Distance:", currentPos);
        telemetry.addData("Current Color", currentLevel);
        telemetry.addData("Target Color", targetLevel);
        telemetry.addData("At Target Color", atTargetLevel());
    }

    private TapeMeasureColor getCurrentLevel() {
        double closestDistance = 1000;
        TapeMeasureColor closetColor = TapeMeasureColor.UNKNOWN;
        for (TapeMeasureColor tapeColor : TapeMeasureColor.getAllColors()) {
            double distance = Math.sqrt(Math.pow(tapeColor.getColor().getRed() - currentColor.getRed(), 2) + Math.pow(tapeColor.getColor().getGreen() - currentColor.getGreen(), 2) + Math.pow(tapeColor.getColor().getBlue() - currentColor.getBlue(), 2));

            if (distance < closestDistance) {
                closestDistance = distance;
                closetColor = tapeColor;
            }
        }

        return closetColor;
    }

    public void stop() {
        leftExtension.stopMotor();
        rightExtension.stopMotor();
    }

    public void reset() {
        leftExtension.resetEncoder();
        rightExtension.resetEncoder();
    }

    public boolean atTargetLevel() {
        reset();
        return targetLevel == currentLevel;
    }

    public double getCurrentPos() {
        return currentPos;
    }
}
