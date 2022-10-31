package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.SecondaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;

@TeleOp
public class UKTeleOp extends OpMode {
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private GamepadEx primaryController, secondaryController;

    private ToggleButtonReader crossToggle;

    private PrimaryRotation primaryRotation;
    private SecondaryRotation secondaryRotation;
    private TertiaryRotation tertiaryRotation;
    private Extension extension;
    private Claw claw;

    private ElapsedTime loopTime;

    @Override
    public void init() {
        // Drive Train
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Subsystems
        primaryRotation = new PrimaryRotation(hardwareMap, telemetry);
        secondaryRotation = new SecondaryRotation(hardwareMap, telemetry);
        tertiaryRotation = new TertiaryRotation(hardwareMap, telemetry, primaryRotation);
        extension = new Extension(hardwareMap, telemetry);
        claw = new Claw(hardwareMap, telemetry);

        // Controllers
        primaryController = new GamepadEx(gamepad1);
        secondaryController = new GamepadEx(gamepad2);

        // Buttons
        crossToggle = new ToggleButtonReader(primaryController, GamepadKeys.Button.A);

        loopTime = new ElapsedTime();
        loopTime.startTime();

        telemetry.addLine("Waiting for start");
        telemetry.update();
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().registerSubsystem(primaryRotation, secondaryRotation,
                                                        tertiaryRotation, extension, claw);
    }

    @Override
    public void loop() {
        loopTime.reset();

        // Primary
        frontLeft.setPower(((gamepad1.left_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
        backLeft.setPower(((gamepad1.left_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));
        frontRight.setPower(((gamepad1.left_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
        backRight.setPower(((gamepad1.left_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));

        CommandScheduler.getInstance().run();

        // Secondary
        if (CommandScheduler.getInstance().requiring(primaryRotation) == null) {
            primaryRotation.rotatePower(gamepad2.left_stick_y);
        }

        // Secondary
        if (CommandScheduler.getInstance().requiring(secondaryRotation) == null) {
            secondaryRotation.rotatePower(gamepad2.right_stick_y);
        }

        // Primary
        if (CommandScheduler.getInstance().requiring(claw) == null) {
            if (crossToggle.getState()) {
                if (claw.getCurrentState() != Claw.State.OPEN) {
                    claw.open();
                }
            } else {
                if (claw.getCurrentState() != Claw.State.CLOSED) {
                    claw.close();
                }
            }
        }

        // Secondary
        if (CommandScheduler.getInstance().requiring(extension) == null) {
            extension.rotatePower(gamepad2.right_trigger - gamepad2.left_trigger);
        }

        telemetry.addData("Loop Time", loopTime.milliseconds());
        telemetry.update();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        super.stop();
    }
}
