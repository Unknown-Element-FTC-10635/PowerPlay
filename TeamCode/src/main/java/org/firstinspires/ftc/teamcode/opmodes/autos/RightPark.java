package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

import java.util.logging.Logger;

@Autonomous(name = "RIGHT - Park", group = "Right")
public class RightPark extends CommandOpMode {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        // Subsystems
        Extension extension = new Extension(hardwareMap, telemetry);
        Rotation rotation = new Rotation(hardwareMap, telemetry);
        LimitSwitch limitSwitch = new LimitSwitch(hardwareMap, telemetry, "primarySwitch");
        LimitSwitch rotationSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationSW");
        Claw claw = new Claw(hardwareMap, telemetry);

        BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Creating Paths");
        telemetry.update();

        Pose2d start = new Pose2d(-36.0, 64, Math.toRadians(270));
        drive.setPoseEstimate(start);

        TrajectorySequence safePosition = drive.trajectorySequenceBuilder(start)
                .lineTo(new Vector2d(-36, 14))
                .build();

        TrajectorySequence orange = drive.trajectorySequenceBuilder(safePosition.end())
                .back(1)
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(safePosition.end())
                .strafeTo(new Vector2d(-8, 15))
                .back(5)
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(safePosition.end())
                .strafeTo(new Vector2d(-60, 16))
                .back(3)
                .build();

        telemetry.addLine("Starting Webcam");
        telemetry.update();

        baseWebcam.startSleeveDetection(true);

        telemetry.addLine("Ready to Start");
        telemetry.update();

        CurrentOpmode.setCurrentOpmode(CurrentOpmode.OpMode.AUTO);

        waitForStart();

        logger.info("Starting Match");

        SleeveDetection.SleeveColor sleeveColor = baseWebcam.getSleeveColor();
        telemetry.addData("Found Sleeve ", sleeveColor);

        telemetry.addLine("Scheduling Tasks");
        telemetry.update();

        register(limitSwitch, extension, claw, rotation, rotationSwitch);

        schedule(
                new SequentialCommandGroup(
                        new CloseClaw(claw),
                        new WaitCommand(500),
                        new FollowTrajectoryCommand(drive, safePosition),
                        new PickPark(drive, sleeveColor, purple, orange, green)
                )
        );

    }
}
