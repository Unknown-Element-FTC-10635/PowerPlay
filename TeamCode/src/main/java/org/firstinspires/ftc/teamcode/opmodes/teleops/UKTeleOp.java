package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class UKTeleOp extends OpMode {
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotorEx rotationLeft, rotationRight, extensionLeft, extensionRight;

    private ElapsedTime loopTime;

    @Override
    public void init() {
        // Drive Train
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        loopTime = new ElapsedTime();
        loopTime.startTime();

        telemetry.addLine("Waiting for start");
        telemetry.update();
    }

    @Override
    public void loop() {
        loopTime.reset();

        // Primary
        frontLeft.setPower(((gamepad1.left_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
        backLeft.setPower(((gamepad1.left_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));
        frontRight.setPower(((gamepad1.left_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
        backRight.setPower(((gamepad1.left_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));

        telemetry.addData("Loop Time", loopTime.milliseconds());
        telemetry.update();
    }
}
