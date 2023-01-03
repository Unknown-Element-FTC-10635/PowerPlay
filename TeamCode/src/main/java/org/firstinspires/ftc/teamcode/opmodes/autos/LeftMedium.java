package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

@Autonomous(name = "LEFT - Medium")
public class LeftMedium extends CommandOpMode {
    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        // Subsystems
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

        TrajectorySequence orange = drive.trajectorySequenceBuilder(start)
                .lineTo(new Vector2d(36, 38))
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(orange.end())
                .lineTo(new Vector2d(11, 38))
                .forward(3)
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(orange.end())
                .lineTo(new Vector2d(63, 38))
                .forward(3)
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

        register(limitSwitch, extension, claw);

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(baseWebcam::stop),
                        new InstantCommand(claw::close),

                        new FollowTrajectoryCommand(drive, orange),
                        new PickPark(drive, sleeveColor, purple, green)
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
