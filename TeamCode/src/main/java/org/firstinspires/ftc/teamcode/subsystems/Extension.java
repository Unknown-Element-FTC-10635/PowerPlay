package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;

@Config
public class Extension extends SubsystemBase {
    public enum TargetLevel {
        SUBSTATION(0, 0),
        LOW(100, 1),
        MEDIUM(400, 2),
        HIGH(1000, 3),
        EXTRA_TALL(1250, 4);

        private final int height;
        private final int order;

        TargetLevel(int height, int order) {
            this.height = height;
            this.order = order;
        }

        public int getHeight() {
            return height;
        }

        public int getOrder() {
            return order;
        }
    }

    private final Telemetry telemetry;

    private final DcMotorEx leftExtension, rightExtension;

    private TargetLevel targetLevel = TargetLevel.SUBSTATION;

    // PID
    private final PIDController leftPIDController;
    private final PIDController rightPIDController;

    public static double pL = 0.00175, iL = 0, dL = 0.00001;
    public static double fL = 0.001;

    public static double pR = 0.006, iR = 0, dR = 0.0001;
    public static double fR = 0.0125;

    private static final double TICKS_PER_DEGREE = 537.7 / 360;

    public Extension(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftExtension = hardwareMap.get(DcMotorEx.class, "leftExtension"); //new Motor(hardwareMap, "leftExtension");
        leftExtension.setDirection(DcMotorSimple.Direction.REVERSE);
        rightExtension = hardwareMap.get(DcMotorEx.class, "rightExtension"); //new Motor(hardwareMap, "rightExtension");

        reset();

        leftPIDController = new PIDController(pL, iL, dL);
        rightPIDController = new PIDController(pR, iR, dR);
    }

    public void upLevel() {
        switch (targetLevel) {
            case SUBSTATION:
            case LOW:
                targetLevel = TargetLevel.MEDIUM;
                break;
            case MEDIUM:
                targetLevel = TargetLevel.HIGH;
                break;
            case HIGH:
                targetLevel = TargetLevel.EXTRA_TALL;
            default:
                break;
        }
    }

    public void downLevel() {
        switch (targetLevel) {
            case LOW:
            case MEDIUM:
                targetLevel = TargetLevel.SUBSTATION;
                break;
            case HIGH:
                targetLevel = TargetLevel.MEDIUM;
                break;
            case EXTRA_TALL:
                targetLevel = TargetLevel.HIGH;
            default:
                break;
        }
    }

    public void setTargetLevel(TargetLevel targetLevel) {
        this.targetLevel = targetLevel;
    }

    public boolean atTargetLevel() {
        double average = (leftExtension.getCurrentPosition() + rightExtension.getCurrentPosition()) / 2.0;
        if (average < targetLevel.getHeight() * 1.01) {
            return true;
        } else if (average > targetLevel.getHeight() * 0.99) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void periodic() {
        if (CurrentOpmode.getCurrentOpmode() == CurrentOpmode.OpMode.TELEOP) {
            movePID(targetLevel.getHeight());
        }

        if (CurrentOpmode.getCurrentOpmode() == CurrentOpmode.OpMode.AUTO) {
        }

        telemetry.addData("Extension Target", targetLevel);
        telemetry.addData("Extension Target Encoder", targetLevel.getHeight());
        telemetry.addData("Left Extension", leftExtension.getCurrentPosition());
        telemetry.addData("Right Extension", rightExtension.getCurrentPosition());
    }


    private void movePID(double level) {
        double cos = Math.cos(Math.toRadians(level / TICKS_PER_DEGREE));

        double pidLeft = leftPIDController.calculate(leftExtension.getCurrentPosition(), level);
        double ffLeft = cos * fL;

        leftExtension.setPower(pidLeft + ffLeft);

        double pidRight = rightPIDController.calculate(rightExtension.getCurrentPosition(), level);
        double ffRight = cos * fR;

        rightExtension.setPower(pidRight + ffRight);
    }

    public void stop() {
        leftExtension.setPower(0);
        rightExtension.setPower(0);
    }

    public void reset() {
        leftExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
