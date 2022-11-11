package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Disabled
@TeleOp
public class TestingTeleop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "fl");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "fr");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "bl");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "br");

        waitForStart();

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
    }
}
