package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.RotatePrimary;
import org.firstinspires.ftc.teamcode.commands.RotateTertiary;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.util.RobotLocation;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

@Autonomous(name = "LEFT - Medium")
public class BlueLeftMedium extends CommandOpMode {
    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        // Subsystems
        PrimaryRotation primaryRotation = new PrimaryRotation(hardwareMap, telemetry);
        TertiaryRotation tertiaryRotation = new TertiaryRotation(hardwareMap, telemetry, primaryRotation);
        Extension extension = new Extension(hardwareMap, telemetry);
        LimitSwitch limitSwitch = new LimitSwitch(hardwareMap, telemetry, "primarySwitch");
        Claw claw = new Claw(hardwareMap, telemetry);

        BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Creating Paths");
        telemetry.update();

        Pose2d start = new Pose2d(36.0, 64, Math.toRadians(270));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .splineTo(new Vector2d(30, 35), Math.toRadians(-125))
                .build();

        TrajectorySequence safePosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .setReversed(true)
                .splineTo(new Vector2d(37, 50), Math.toRadians(90))
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(safePosition.end())
                .lineTo(new Vector2d(38, 15))
                .turn(Math.toRadians(-90))
                .lineTo(new Vector2d(11, 15))
                .turn(Math.toRadians(-95))
                .forward(5)
                .build();

        TrajectorySequence orange = drive.trajectorySequenceBuilder(safePosition.end())
                .lineTo(new Vector2d(35, 15))
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(safePosition.end())
                .lineTo(new Vector2d(37, 15))
                .turn(Math.toRadians(-90))
                .lineTo(new Vector2d(61, 15))
                .build();

        telemetry.addLine("Starting Webcam");
        telemetry.update();

        baseWebcam.startSleeveDetection();

        telemetry.addLine("Ready to Start");
        telemetry.update();

        waitForStart();

        SleeveDetection.SleeveColor sleeveColor = baseWebcam.getSleeveColor();
        telemetry.addData("Found Sleeve ", sleeveColor);

        telemetry.addLine("Scheduling Tasks");
        telemetry.update();

        register(primaryRotation, tertiaryRotation, limitSwitch, extension, claw);

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(baseWebcam::stop),
                        new InstantCommand(claw::open),
                        new InstantCommand(claw::close),
                        //new InstantCommand(tertiaryRotation::goToInitialPosition)
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new InstantCommand(tertiaryRotation::goToInitialPosition),
                                new RotatePrimary(primaryRotation, extension, 150, -0.6)
                        ),
                        new RotateTertiary(tertiaryRotation, 0.75),
                        new InstantCommand(claw::open),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, safePosition),
                                new SequentialCommandGroup(
                                        new WaitCommand(300),
                                        new InstantCommand(claw::close)
                                )
                        ),
                        new RotateTertiary(tertiaryRotation, 0.5),
                        new PickPark(drive, sleeveColor, purple, orange, green)
                        /*
                        new UnfoldArm(primaryRotation, tertiaryRotation, extension, limitSwitch),
                        new WaitCommand(500),
                        new InstantCommand(() -> primaryRotation.setSlowLift(true)),
                        new RotatePrimary(primaryRotation, extension,100, -0.4)
                         */
                )
        );

    }
}
