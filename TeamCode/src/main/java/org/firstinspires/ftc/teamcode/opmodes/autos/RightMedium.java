package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.HighGoal;
import org.firstinspires.ftc.teamcode.commandgroups.MediumGoal;
import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commandgroups.RotateHome;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.FaceConeStack;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.OpenClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

@Autonomous(name = "RIGHT - Medium")
public class RightMedium extends CommandOpMode {
    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        // Subsystems
        Extension extension = new Extension(hardwareMap, telemetry);
        Rotation rotation = new Rotation(hardwareMap, telemetry);
        LimitSwitch limitSwitch = new LimitSwitch(hardwareMap, telemetry, "primarySwitch");
        Claw claw = new Claw(hardwareMap, telemetry);

        BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Creating Paths");
        telemetry.update();

        Pose2d start = new Pose2d(-36.0, 64, Math.toRadians(270));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .splineTo(new Vector2d(-30, 37), Math.toRadians(-54))
                .forward(4)
                .build();

        TrajectorySequence safePosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .setReversed(true)
                .splineTo(new Vector2d(-35, 50), Math.toRadians(90))
                .build();

        TrajectorySequence stackStart = drive.trajectorySequenceBuilder(safePosition.end())
                .lineTo(new Vector2d(-35, 2))
                .lineTo(new Vector2d(-35, 14))
                .turn(Math.toRadians(-92))
                .build();

        TrajectorySequence approachStack = drive.trajectorySequenceBuilder(stackStart.end())
                .forward(5)
                .build();

        TrajectorySequence approachPole = drive.trajectorySequenceBuilder(approachStack.end())
                //.setReversed(true)
                .setReversed(true)
                .splineTo(new Vector2d(-35, 15), Math.toRadians(-140))
                .forward(4)
                //.turn(Math.toRadians(135))
                .build();

        TrajectorySequence orange = drive.trajectorySequenceBuilder(approachPole.end())
                .back(4)
                .turn(Math.toRadians(-43))
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(orange.end())
                .lineTo(new Vector2d(-11, 12))
                .strafeLeft(5)
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(orange.end())
                .lineTo(new Vector2d(-60, 12))
                .strafeLeft(5)
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

        register(limitSwitch, extension, claw, rotation);

        schedule(
                new SequentialCommandGroup(
                        new OpenClaw(claw),
                        new CloseClaw(claw),
                        new WaitCommand(250),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new MediumGoal(rotation, claw)
                        ),
                        new WaitCommand(300),
                        new OpenClaw(claw),
                        new FollowTrajectoryCommand(drive, safePosition),
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                        new WaitCommand(200),
                                        new RotateHome(rotation)
                                ),
                                new FollowTrajectoryCommand(drive, stackStart),
                                new InstantCommand(baseWebcam::switchPipelineConeStack)
                        ),
                        new FaceConeStack(drive, baseWebcam),
                        new OpenClaw(claw),
                        new Rotate(rotation, 30, 0.05),
                        new FollowTrajectoryCommand(drive, approachStack),
                        new ParallelCommandGroup(
                                new WaitCommand(250),
                                new CloseClaw(claw)
                        ),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, approachPole),
                                new MediumGoal(rotation, claw)
                        ),
                        new ParallelCommandGroup(
                            new WaitCommand(1000),
                            new OpenClaw(claw)
                        ),
                        //new FollowTrajectoryCommand(drive, orange),
                        new PickPark(drive, sleeveColor, purple, green)
                        /*
                        new PickPark(drive, sleeveColor, purple, green)
                         */
                )
        );

    }
}
