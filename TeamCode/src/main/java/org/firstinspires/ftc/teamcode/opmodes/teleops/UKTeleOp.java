package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commandgroups.UnfoldArm;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;
import org.firstinspires.ftc.teamcode.util.SubsystemState;

@TeleOp
public class UKTeleOp extends OpMode {
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private PrimaryRotation primaryRotation;
    private TertiaryRotation tertiaryRotation;
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

    private double wheelMultiplier = 0.75;

    @Override
    public void init() {
        // Drive Train
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Subsystems
        primaryRotation = new PrimaryRotation(hardwareMap, telemetry);
        tertiaryRotation = new TertiaryRotation(hardwareMap, telemetry, primaryRotation);
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
        CommandScheduler.getInstance().registerSubsystem(primaryRotation,
                                                        tertiaryRotation, limitSwitch, extension, claw);
        //tertiaryRotation.setBeginAdjustment(true);
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

        CommandScheduler.getInstance().run();

        // Secondary
        if (CommandScheduler.getInstance().requiring(primaryRotation) == null) {
            if (Math.abs(gamepad2.left_stick_y) > 0.1) {
                if (primaryPreviouslyMoving) {
                    primaryRotation.setState(SubsystemState.MOVING);
                } else {
                    primaryRotation.setState(SubsystemState.STARTING);
                    primaryPreviouslyMoving = true;
                }

                primaryRotation.rotatePower(gamepad2.left_stick_y);

            } else {
                primaryRotation.stop();
                if (primaryPreviouslyMoving) {
                    primaryRotation.setState(SubsystemState.STOPPING);
                    primaryPreviouslyMoving = false;
                } else if (primaryRotation.getState() != SubsystemState.UNKNOWN) {
                    primaryRotation.setState(SubsystemState.STOPPED);
                }
            }
        }

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
            extension.rotatePower((gamepad2.right_trigger - gamepad2.left_trigger) / 2);
        }


        if (currentGamepad1.triangle && !previousGamepad1.triangle) {
            adjustmentToggle = !adjustmentToggle;
        }

        // Secondary
        if (CommandScheduler.getInstance().requiring(tertiaryRotation) == null) {
            if (adjustmentToggle) {
                tertiaryRotation.setBeginAdjustment(true);
            } else {
                tertiaryRotation.setBeginAdjustment(false);
            }

            if (!adjustmentToggle) {
                if (gamepad1.right_bumper) {
                    tertiaryRotation.increasePositiveDirection();
                } else if (gamepad1.left_bumper) {
                    tertiaryRotation.increaseNegativeDirection();
                }
            }
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

        if (limitSwitch.isPressed()) {
            primaryRotation.reset();
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
