package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commandgroups.HighGoal;
import org.firstinspires.ftc.teamcode.commandgroups.LowGoal;
import org.firstinspires.ftc.teamcode.commandgroups.MediumGoal;
import org.firstinspires.ftc.teamcode.commandgroups.Substation;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

@TeleOp
public class UKTeleOp extends OpMode {
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private LimitSwitch limitSwitch;
    private Extension extension;
    private Rotation rotation;
    private Claw claw;

    private ElapsedTime loopTime;

    private final Gamepad currentGamepad1 = new Gamepad();
    private final Gamepad currentGamepad2 = new Gamepad();
    private final Gamepad previousGamepad1 = new Gamepad();
    private final Gamepad previousGamepad2 = new Gamepad();

    private boolean speedToggle = false;
    private double wheelMultiplier = 0.8;

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
        rotation = new Rotation(hardwareMap, telemetry);
        claw = new Claw(hardwareMap, telemetry);

        loopTime = new ElapsedTime();
        loopTime.startTime();
        telemetry.addLine("Waiting for start");
        telemetry.update();
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().registerSubsystem(rotation, limitSwitch, extension, claw);

        claw.open();
    }

    @Override
    public void loop() {
        loopTime.reset();

        try {
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addLine("Failed to copy");
        }

        // Primary
        backRight.setPower(((gamepad1.left_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x) * wheelMultiplier);
        frontLeft.setPower(((gamepad1.left_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x) * wheelMultiplier);
        backLeft.setPower(((gamepad1.left_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x) * wheelMultiplier);
        frontRight.setPower(((gamepad1.left_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x) * wheelMultiplier);


        if (currentGamepad1.triangle && !previousGamepad1.triangle) {
            CommandScheduler.getInstance().schedule(new HighGoal(rotation, extension, claw));
        }

        if (currentGamepad1.square && !previousGamepad1.square) {
            CommandScheduler.getInstance().schedule(new LowGoal(rotation, claw));
        }

        if (currentGamepad1.circle && !previousGamepad1.circle) {
            CommandScheduler.getInstance().schedule(new MediumGoal(rotation, claw));
        }

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            CommandScheduler.getInstance().schedule(new Substation(rotation, extension, limitSwitch, claw));
        }


        CommandScheduler.getInstance().run();


        // Primary
        if (CommandScheduler.getInstance().requiring(claw) == null) {
            if (currentGamepad1.cross && !previousGamepad1.cross) {
                if (claw.getCurrentState() == Claw.State.OPEN) {
                    claw.close();
                } else if (claw.getCurrentState() == Claw.State.CLOSED) {
                    claw.open();
                }
                //clawToggle = !clawToggle;
            }
        }

        // Primary
        if (currentGamepad1.right_stick_button && previousGamepad1.right_stick_button) {
            speedToggle = !speedToggle;
            if (speedToggle) {
                wheelMultiplier = 1;
            } else {
                wheelMultiplier = 0.8;
            }
        }

        if (CommandScheduler.getInstance().requiring(rotation) == null) {
            rotation.manualRotation(gamepad1.right_trigger - gamepad1.left_trigger);
        }

        if (CommandScheduler.getInstance().requiring(extension) == null) {
            if (gamepad1.right_bumper) {
                extension.rotatePower(-1f);
            } else if (gamepad1.left_bumper) {
                extension.rotatePower(0.5f);
            } else {
                extension.stop();
            }
        }

        telemetry.addData("Loop Time", loopTime.milliseconds());
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        super.stop();
    }
}
