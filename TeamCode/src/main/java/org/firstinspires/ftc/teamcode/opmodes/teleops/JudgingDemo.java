package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commandgroups.HighGoal;
import org.firstinspires.ftc.teamcode.commandgroups.LowGoal;
import org.firstinspires.ftc.teamcode.commandgroups.MediumGoal;
import org.firstinspires.ftc.teamcode.commandgroups.Substation;
import org.firstinspires.ftc.teamcode.commands.FaceConeStack;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;

@Disabled
@TeleOp
public class JudgingDemo extends OpMode {
    private LimitSwitch extensionLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch;
    private Extension extension;
    private Rotation rotation;
    private Claw claw;

    private final Gamepad currentGamepad1 = new Gamepad();
    private final Gamepad currentGamepad2 = new Gamepad();
    private final Gamepad previousGamepad1 = new Gamepad();
    private final Gamepad previousGamepad2 = new Gamepad();

    private BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

    private boolean openCVDection = false;

    private SampleMecanumDrive drive;

    @Override
    public void init() {
        // Subsystems
        extensionLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionSW");
        rotationBottomLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationBSW");
        rotationTopLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationTSW");
        extension = new Extension(hardwareMap, telemetry);
        rotation = new Rotation(hardwareMap, telemetry);
        claw = new Claw(hardwareMap, telemetry);

        drive = new SampleMecanumDrive(hardwareMap);

        baseWebcam.startStackDetectionBlue();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        try {
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addLine("Failed to copy");
        }

        if (currentGamepad1.triangle && !previousGamepad1.triangle) {
            CommandScheduler.getInstance().schedule(new HighGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extension, extensionLimitSwitch, claw));
        }

        if (currentGamepad1.square && !previousGamepad1.square) {
            CommandScheduler.getInstance().schedule(new LowGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }

        if (currentGamepad1.circle && !previousGamepad1.circle) {
            CommandScheduler.getInstance().schedule(new MediumGoal(rotation, extension, extensionLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            CommandScheduler.getInstance().schedule(new Substation(rotation, extension, extensionLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw));
        }

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_up) {
            openCVDection = !openCVDection;

        }

        if (openCVDection) {
            CommandScheduler.getInstance().schedule(new FaceConeStack(drive, baseWebcam, false));
        }

        CommandScheduler.getInstance().run();
    }

    @Override
    public void stop() {
        super.stop();
    }


}
