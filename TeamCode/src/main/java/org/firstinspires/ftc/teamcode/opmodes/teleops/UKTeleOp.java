package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;

@TeleOp
public class UKTeleOp extends OpMode {
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private LimitSwitch limitSwitch;
    private Extension extension;
    private Claw claw;

    private ElapsedTime loopTime;

    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad currentGamepad2 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();
    private Gamepad previousGamepad2 = new Gamepad();

    private boolean primaryPreviouslyMoving = false;
    private boolean clawToggle = false;
    private boolean adjustmentToggle = false;
    private boolean speedToggle = false;
    private boolean positionToggle = false;

    private double wheelMultiplier = 1;
    private int robotDirection = 1;

    @Override
    public void init() {
        // Drive Train
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backRight = hardwareMap.get(DcMotor.class, "br");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Subsystems
        limitSwitch = new LimitSwitch(hardwareMap, telemetry, "primarySwitch");
        extension = new Extension(hardwareMap, telemetry);
        claw = new Claw(hardwareMap, telemetry);

        loopTime = new ElapsedTime();
        loopTime.startTime();

        telemetry.addLine("Waiting for start");
        telemetry.update();
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().registerSubsystem(limitSwitch, extension, claw);

        claw.open();
        //tertiaryRotation.setBeginAdjustment(true);
    }

    @Override
    public void loop() {
        loopTime.reset();

        //if (claw.getCurrentState() == Claw.State.OPEN) {
        //    wheelMultiplier = -1;
        //} else if (claw.getCurrentState() == Claw.State.CLOSED) {
        //    wheelMultiplier = 1;
        //}

        try {
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addLine("Failed to copy");
        }

        // Primary
        backRight.setPower((((gamepad1.left_stick_y * robotDirection) - gamepad1.left_stick_x) + gamepad1.right_stick_x) * wheelMultiplier);
        frontLeft.setPower((((gamepad1.left_stick_y * robotDirection) - gamepad1.left_stick_x) - gamepad1.right_stick_x) * wheelMultiplier);
        backLeft.setPower((((gamepad1.left_stick_y * robotDirection) + gamepad1.left_stick_x) - gamepad1.right_stick_x) * wheelMultiplier * robotDirection);
        frontRight.setPower((((gamepad1.left_stick_y * robotDirection) + gamepad1.left_stick_x) + gamepad1.right_stick_x) * wheelMultiplier * robotDirection);

        CommandScheduler.getInstance().run();

        if (currentGamepad1.cross && !previousGamepad1.cross) {
            clawToggle = !clawToggle;
        }

        // Primary
        if (CommandScheduler.getInstance().requiring(claw) == null) {
            if (clawToggle) {
                if (claw.getCurrentState() != Claw.State.OPEN) {
                    claw.open();
                }
            } else {
                if (claw.getCurrentState() != Claw.State.CLOSED) {
                    claw.close();
                }
            }
        }

        // Primary
        if (CommandScheduler.getInstance().requiring(extension) == null) {
            extension.rotatePower((gamepad2.left_trigger - gamepad2.right_trigger) / (float)1.5);
        }


        if (currentGamepad1.triangle && !previousGamepad1.triangle) {
            adjustmentToggle = !adjustmentToggle;
        }

        // Primary
        if (currentGamepad1.right_stick_button && previousGamepad1.right_stick_button) {
            speedToggle = !speedToggle;
            if (speedToggle) {
                wheelMultiplier = 1;
            } else {
                wheelMultiplier = 0.75;
            }
        }

        telemetry.addData("Adjusting", adjustmentToggle);
        telemetry.addData("Loop Time", loopTime.milliseconds());
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        super.stop();
    }
}
