package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Config
@TeleOp
public class TestingTeleop extends OpMode {
    private PIDController pidController1, pidController2;
    private DcMotorEx leftExtension, rightExtension;

    public static double pL = 0.0025, iL = 0, dL = 0.00001;
    public static double fL = 0.01;

    public static double pR = 0.002, iR = 0, dR = 0.000005;
    public static double fR = 0.0022;

    public static int target = 0;

    private static final double TICKS_PER_DEGREE = 384.5 / 360;

    @Override
    public void init() {
        pidController1 = new PIDController(pL, iL, dL);
        pidController2 = new PIDController(pR, iR, dR);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        leftExtension = hardwareMap.get(DcMotorEx.class, "leftExtension");
        rightExtension = hardwareMap.get(DcMotorEx.class, "rightExtension");
        rightExtension.setDirection(DcMotorSimple.Direction.REVERSE);

        leftExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @java.lang.Override
    public void loop() {
        pidController1.setPID(pL, iL, dL);
        int armPos1 = leftExtension.getCurrentPosition();
        double pid1 = pidController1.calculate(armPos1, target);
        double ff1 = Math.cos(Math.toRadians(target / TICKS_PER_DEGREE)) * fL;

        double power1 = pid1 + ff1;
        leftExtension.setPower(power1);

        pidController2.setPID(pR, iR, dR);
        int armPos2 = rightExtension.getCurrentPosition();
        double pid2 = pidController2.calculate(armPos2, target);
        double ff2 = Math.cos(Math.toRadians(target / TICKS_PER_DEGREE)) * fR;

        double power2 = pid2 + ff2;
        rightExtension.setPower(power2);

        telemetry.addData("Position Left", armPos1);
        telemetry.addData("Position Right", armPos2);
        telemetry.addData("Target", target);
        telemetry.update();
    }
}