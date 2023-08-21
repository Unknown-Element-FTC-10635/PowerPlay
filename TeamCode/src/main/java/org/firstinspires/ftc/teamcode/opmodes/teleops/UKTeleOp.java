package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.arcrobotics.ftclib.command.Command;
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
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;

@TeleOp
public class UKTeleOp extends OpMode {
    private static final double CLAW_CHANGE_STATE_ANGLE = 110;

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private LimitSwitch extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch;
    private ColorSensor colorSensors;
    private Extension extension;
    private Rotation rotation;
    private Claw claw;

    private ElapsedTime loopTime;

    private final Gamepad currentGamepad1 = new Gamepad();
    private final Gamepad currentGamepad2 = new Gamepad();
    private final Gamepad previousGamepad1 = new Gamepad();
    private final Gamepad previousGamepad2 = new Gamepad();

    private boolean speedToggle = false;
    private double wheelMultiplier = 0.4;

    private boolean rumbleToggle = true;

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
        extensionLeftLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionLSW");
        extensionRightLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionRSW");
        rotationBottomLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationBSW");
        rotationTopLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationTSW");
        //colorSensors = new ColorSensor(hardwareMap, telemetry);
        extension = new Extension(hardwareMap, telemetry);
        rotation = new Rotation(hardwareMap, telemetry);
        claw = new Claw(hardwareMap, telemetry);

        extensionLeftLimitSwitch.setInverted(true);

        loopTime = new ElapsedTime();
        loopTime.startTime();
        telemetry.addLine("Waiting for start");
        telemetry.update();
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().registerSubsystem(rotation, rotationBottomLimitSwitch, extensionLeftLimitSwitch, extensionRightLimitSwitch, extension, claw, rotationTopLimitSwitch);

        extension.resetLeft();
        extension.resetRight();
        claw.openBig();
    }

    @Override
    public void loop() {
        loopTime.reset();

        CommandScheduler commandScheduler = CommandScheduler.getInstance();

        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        // Primary
        double forward = currentGamepad1.left_stick_y;
        double turn = currentGamepad1.left_stick_x;
        double strafe = currentGamepad1.right_stick_x;

        backRight.setPower(((forward - turn) + strafe) * wheelMultiplier);
        frontLeft.setPower(((forward - turn) - strafe) * wheelMultiplier);
        backLeft.setPower(((forward + turn) - strafe) * wheelMultiplier);
        frontRight.setPower(((forward + turn) + strafe) * wheelMultiplier);

        /*
        if (currentGamepad1.triangle && !previousGamepad1.triangle) {
            commandScheduler.schedule(new HighGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, claw));
        }

        if (currentGamepad2.triangle && !previousGamepad2.triangle) {
            commandScheduler.schedule(new HighGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, claw));
        }

        if (currentGamepad1.square && !previousGamepad1.square) {
            commandScheduler.schedule(new LowGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }

        if (currentGamepad1.circle && !previousGamepad1.circle) {
            commandScheduler.schedule(new MediumGoal(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            commandScheduler.schedule(new Substation(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }

        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
            commandScheduler.schedule(new Substation(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }*/

        commandScheduler.run();


        // Primary
        if (commandScheduler.requiring(claw) == null) {
            if (currentGamepad1.cross && !previousGamepad1.cross) {
                if (claw.getCurrentState() == Claw.State.OPEN) {
                    claw.close();
                } else if (claw.getCurrentState() == Claw.State.CLOSED) {
                    if (rotationTopLimitSwitch.isPressed()) {
                        claw.openSmall();
                    } else {
                        claw.openBig();
                    }
                }
            }
        }

        // Primary
        if (currentGamepad1.right_stick_button && !previousGamepad1.right_stick_button) {
            speedToggle = !speedToggle;
            if (speedToggle) {
                wheelMultiplier = 0.8;
            } else {
                wheelMultiplier = 0.4;
            }
        }

        if (commandScheduler.requiring(rotation) == null) {
            if (rotationTopLimitSwitch.isPressed()) {
                rotation.manualRotation(-gamepad2.left_trigger);
            } else if (rotationBottomLimitSwitch.isPressed()) {
                rotation.manualRotation(gamepad2.right_trigger);
            } else {
                rotation.manualRotation(gamepad2.right_trigger - gamepad2.left_trigger);
            }
        }

        if (commandScheduler.requiring(extension) == null) {
            if (extension.isOverride()) {
                if (gamepad2.right_bumper) {
                    extension.moveManual(1.0);
                } else if (gamepad2.left_bumper && !extensionLeftLimitSwitch.isPressed() && !extensionLeftLimitSwitch.isPressed()) {
                    extension.moveManual(-1.0);
                } else {
                    extension.stop();
                }
            } else {
                if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                    extension.upLevel();
                } else if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                    extension.downLevel();
                }
            }
        }

        if (rotationBottomLimitSwitch.isPressed() && claw.getCurrentOpenness() == Claw.Open.SMALL && claw.getCurrentState() == Claw.State.OPEN) {
            claw.openBig();
        } else if (rotationTopLimitSwitch.isPressed() && claw.getCurrentOpenness() == Claw.Open.BIG && claw.getCurrentState() == Claw.State.OPEN) {
            claw.openSmall();
        }

        if (rotationBottomLimitSwitch.isPressed()) {
            rotation.reset();
        }

        if (currentGamepad2.square && !previousGamepad2.square) {
            extension.setOverride(true);
        }

        if (extensionLeftLimitSwitch.isPressed()) {
            extension.resetLeft();
        }

        if (extensionRightLimitSwitch.isPressed()) {
            extension.resetRight();
        }

        /*
        if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
            rumbleToggle = !rumbleToggle;
        }

        if (rumbleToggle && !currentGamepad1.isRumbling()) {
            if (colorSensors.leftNotGreen() && colorSensors.rightNotGreen()) {
                gamepad1.rumble(1, 1, 100);
            } else if (colorSensors.leftNotGreen()) {
                gamepad1.rumble(1, 0, 100);
            } else if (colorSensors.rightNotGreen()) {
                gamepad1.rumble(0, 1, 100);
            }
        }

        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
            rotation.setEnableSlowdown(!rotation.isEnableSlowdown());
        }
        */

        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
            throw new UnsupportedOperationException();
        }

        telemetry.addData("Loop Time", loopTime.milliseconds());
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        super.stop();
    }
}
