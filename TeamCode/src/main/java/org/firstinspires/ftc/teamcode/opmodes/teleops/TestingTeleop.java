package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class TestingTeleop extends OpMode {
    private PIDController pidController;
    private DcMotorEx leftExtension, rightExtension;


    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;

    private static final double TICKS_PER_DEGREE = 537.7 / 360;

    @java.lang.Override
    public void init() {
        pidController = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        leftExtension = hardwareMap.get(DcMotorEx.class, "leftExtension");
        rightExtension = hardwareMap.get(DcMotorEx.class, "rightExtension");
    }

    @java.lang.Override
    public void loop() {
        pidController.setPID(p, i, d);
        int armPos = (leftExtension.getCurrentPosition() + rightExtension.getCurrentPosition()) / 2;
        double pid = pidController.calculate(armPos, target);
        double ff = Math.cos(Math.toRadians(target / TICKS_PER_DEGREE)) * f;

        double power = pid + f;

        leftExtension.setPower(power);
        rightExtension.setPower(power);

        telemetry.addData("Position", armPos);
        telemetry.addData("Target", target);
        telemetry.update();
    }
}
