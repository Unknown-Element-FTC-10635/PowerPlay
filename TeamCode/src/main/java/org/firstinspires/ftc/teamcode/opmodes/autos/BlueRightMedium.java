package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.commandgroups.UnfoldArm;
import org.firstinspires.ftc.teamcode.commands.ExtendSlides;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.SecondaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

@Autonomous(name = "BLUE (Right) - Medium")
public class BlueRightMedium extends CommandOpMode {

    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        // Subsystems
        PrimaryRotation primaryRotation = new PrimaryRotation(hardwareMap, telemetry);
        SecondaryRotation secondaryRotation = new SecondaryRotation(hardwareMap, telemetry);
        TertiaryRotation tertiaryRotation = new TertiaryRotation(hardwareMap, telemetry, primaryRotation);
        Extension extension = new Extension(hardwareMap, telemetry);
        Claw claw = new Claw(hardwareMap, telemetry);

        BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Creating Paths");
        telemetry.update();

        Pose2d start = new Pose2d(-36.0, 64, Math.toRadians(270));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .splineTo(new Vector2d(-33, 41), Math.toRadians(-63))
                .build();

        TrajectorySequence cyclePosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .splineTo(new Vector2d(-45.33, 16.63), Math.toRadians(192.50))
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

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(baseWebcam::stop),
                        new InstantCommand(primaryRotation::reset),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new UnfoldArm(primaryRotation, secondaryRotation, tertiaryRotation)
                        ),
                        new ExtendSlides(extension, 7, 0.5),
                        new InstantCommand(claw::open)
                )
        );

        register(primaryRotation, secondaryRotation, tertiaryRotation, extension, claw);
    }
}
