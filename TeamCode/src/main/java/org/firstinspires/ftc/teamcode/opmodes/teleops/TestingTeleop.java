package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@Disabled
@TeleOp
public class TestingTeleop extends LinearOpMode {
    private PIDController pidController1, pidController2;
    private DcMotorEx leftExtension, rightExtension;

    public static double pL = 0.00175, iL = 0, dL = 0.00001;
    public static double fL = 0.001;

    public static double pR = 0.006, iR = 0, dR = 0.0001;
    public static double fR = 0.0125;

    public static int target = 0;

    private static final double TICKS_PER_DEGREE = 537.7 / 360;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "fl");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "fr");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "bl");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "br");

        waitForStart();

        frontLeft.setPower(0.5);
        Thread.sleep(500);
        frontLeft.setPower(0);
        backLeft.setPower(0.5);
        Thread.sleep(500);
        backLeft.setPower(0);
        frontRight.setPower(0.5);
        Thread.sleep(500);
        frontRight.setPower(0);
        backRight.setPower(0.5);
        Thread.sleep(500);
        backRight.setPower(0);

        Thread.sleep(1000);

        frontLeft.setPower(0.25);
        Thread.sleep(500);
        frontLeft.setPower(0);
        backLeft.setPower(0.25);
        Thread.sleep(500);
        backLeft.setPower(0);
        frontRight.setPower(0.25);
        Thread.sleep(500);
        frontRight.setPower(0);
        backRight.setPower(0.25);
        Thread.sleep(500);
        backRight.setPower(0);

        Thread.sleep(1000);

        frontLeft.setPower(0.1);
        Thread.sleep(500);
        frontLeft.setPower(0);
        backLeft.setPower(0.1);
        Thread.sleep(500);
        backLeft.setPower(0);
        frontRight.setPower(0.1);
        Thread.sleep(500);
        frontRight.setPower(0);
        backRight.setPower(0.1);
        Thread.sleep(500);
        backRight.setPower(0);
    }
}
