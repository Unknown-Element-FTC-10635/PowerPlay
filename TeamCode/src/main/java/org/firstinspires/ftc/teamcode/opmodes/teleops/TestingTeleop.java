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

import org.firstinspires.ftc.teamcode.subsystems.Extension;

@Config
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
        //DcMotorEx leftExtesion = hardwareMap.get(DcMotorEx.class, "leftExtension");
        //DcMotorEx rightExtension = hardwareMap.get(DcMotorEx.class, "rightExtension");

        Extension extension = new Extension(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                extension.moveManual(1);
            } else if (gamepad1.left_bumper) {
                extension.moveManual(-0.5);
            } else {
                extension.moveManual(0);
            }
        }
    }
}
