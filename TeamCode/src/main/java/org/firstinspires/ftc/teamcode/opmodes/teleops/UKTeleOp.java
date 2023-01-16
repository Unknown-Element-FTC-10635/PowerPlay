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
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;

@TeleOp
public class UKTeleOp extends OpMode {
    private static final double CLAW_CHANGE_STATE_ANGLE = 140;

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private LimitSwitch extensionLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch;
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
        CurrentOpmode.setCurrentOpmode(CurrentOpmode.OpMode.TELEOP);

        // Drive Train
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backRight = hardwareMap.get(DcMotor.class, "br");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Subsystems
        extensionLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "primarySwitch");
        rotationBottomLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationBSW");
        rotationTopLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationTSW");
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
        CommandScheduler.getInstance().registerSubsystem(rotation, rotationBottomLimitSwitch, extensionLimitSwitch, extension, claw);

        claw.openBig();
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
            CommandScheduler.getInstance().schedule(new HighGoal(rotation, rotationBottomLimitSwitch, extension, claw));
        }

        if (currentGamepad1.square && !previousGamepad1.square) {
            CommandScheduler.getInstance().schedule(new LowGoal(rotation, rotationBottomLimitSwitch, claw));
        }

        if (currentGamepad1.circle && !previousGamepad1.circle) {
            CommandScheduler.getInstance().schedule(new MediumGoal(rotation, rotationBottomLimitSwitch, claw));
        }

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            CommandScheduler.getInstance().schedule(new Substation(rotation, extension, extensionLimitSwitch, rotationBottomLimitSwitch, claw));
        }


        CommandScheduler.getInstance().run();


        // Primary
        if (CommandScheduler.getInstance().requiring(claw) == null) {
            if (currentGamepad1.cross && !previousGamepad1.cross) {
                if (claw.getCurrentState() == Claw.State.OPEN) {
                    claw.close();
                } else if (claw.getCurrentState() == Claw.State.CLOSED) {
                    if (rotation.getAngle() > CLAW_CHANGE_STATE_ANGLE) {
                        claw.openSmall();
                    } else {
                        claw.openBig();
                    }
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
            if (gamepad2.right_trigger - gamepad2.left_trigger != 0) {
                if (rotationTopLimitSwitch.isPressed()) {
                    rotation.manualRotation(-gamepad2.left_trigger);
                } else if (rotationBottomLimitSwitch.isPressed()) {
                    rotation.manualRotation(gamepad2.right_trigger);
                } else {
                    rotation.manualRotation(gamepad2.right_trigger - gamepad2.left_trigger);
                }

            } else {
                rotation.stop();
            }
        }

        if (CommandScheduler.getInstance().requiring(extension) == null) {
            if (gamepad2.right_bumper) {
                extension.rotatePower(-1f);
            } else if (gamepad2.left_bumper) {
                extension.rotatePower(0.5f);
            } else {
                extension.stop();
            }
        }

        if (rotationBottomLimitSwitch.isPressed()) {
            rotation.reset();
        }

        if (rotation.getAngle() < CLAW_CHANGE_STATE_ANGLE && claw.getCurrentState() == Claw.State.OPEN) {
            claw.openBig();
        } else if (rotation.getAngle() > CLAW_CHANGE_STATE_ANGLE && claw.getCurrentState() == Claw.State.OPEN) {
            claw.openSmall();
        }

        telemetry.addData("Loop Time", loopTime.milliseconds());
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        super.stop();
    }
}
