package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.MediumGoal;
import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commandgroups.RotateHome;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
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
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

import java.util.logging.Logger;

@Autonomous(name = "LEFT - Medium")
public class LeftMedium extends CommandOpMode {
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

        Pose2d start = new Pose2d(-36.0, -64, Math.toRadians(90));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .splineTo(new Vector2d(-32, -37), Math.toRadians(54))
                .forward(6)
                .build();

        TrajectorySequence safePosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .setReversed(true)
                .splineTo(new Vector2d(-35, -50), Math.toRadians(-89))
                .build();

        TrajectorySequence stackStart = drive.trajectorySequenceBuilder(safePosition.end())
                .lineTo(new Vector2d(-36, -4))
                .lineTo(new Vector2d(-36, -14))
                .turn(Math.toRadians(100))
                .build();

        TrajectorySequence approachStack = drive.trajectorySequenceBuilder(stackStart.end())
                .lineTo(new Vector2d(-58, -16))
                .build();

        TrajectorySequence approachPole = drive.trajectorySequenceBuilder(approachStack.end())
                .back(8)
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(-40, -14, Math.toRadians(-40)))
                .lineTo(new Vector2d(-37, -16))
                .forward(9)
                .build();

        TrajectorySequence backAwayPole = drive.trajectorySequenceBuilder(approachPole.end())
                .back(5)
                .lineToLinearHeading(new Pose2d(-38, -15, Math.toRadians(-5)))
                .build();

        TrajectorySequence orange = drive.trajectorySequenceBuilder(backAwayPole.end())
                .forward(4)
                .strafeRight(5)
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(backAwayPole.end())
                .lineTo(new Vector2d(-10, -18))
                .strafeRight(5)
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(backAwayPole.end())
                .lineTo(new Vector2d(-60, -17))
                .strafeRight(3)
                .build();

        telemetry.addLine("Starting Webcam");
        telemetry.update();

        baseWebcam.startSleeveDetection();

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
                        new InstantCommand(baseWebcam::stop),
                        new OpenClaw(claw),
                        new CloseClaw(claw),
                        new WaitCommand(500),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new MediumGoal(rotation, rotationSwitch, claw)
                        ),
                        new WaitCommand(300),
                        new OpenClaw(claw),
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                        new WaitCommand(300),
                                        new RotateHome(rotation, rotationSwitch)
                                ),
                                new FollowTrajectoryCommand(drive, safePosition)
                                //new InstantCommand(baseWebcam::switchPipelineConeStack)
                        ),
                        new FollowTrajectoryCommand(drive, stackStart),
                        new OpenClaw(claw),
                        new Rotate(rotation, rotationSwitch, 30, 0.1),
                        new FollowTrajectoryCommand(drive, approachStack),
                        new ParallelCommandGroup(
                                new WaitCommand(250),
                                new CloseClaw(claw)
                        ),
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                        new WaitCommand(50),
                                        new Rotate(rotation, rotationSwitch, 90, 0.2),
                                        new Rotate(rotation, rotationSwitch, 125, 0.05)
                                ),
                                new FollowTrajectoryCommand(drive, approachPole)
                        ),
                        new WaitCommand(500),
                        new OpenClaw(claw),
                        new WaitCommand(250),
                        new FollowTrajectoryCommand(drive, backAwayPole),
                        new PickPark(drive, sleeveColor, purple, orange, green),
                        new RotateHome(rotation, rotationSwitch)
                )
        );

    }
}
