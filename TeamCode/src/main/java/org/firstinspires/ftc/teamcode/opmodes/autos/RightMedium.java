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
import org.firstinspires.ftc.teamcode.commandgroups.Substation;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.OpenClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;
import org.firstinspires.ftc.teamcode.util.lift.ConeStackLevel;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

import java.util.logging.Logger;

@Autonomous(name = "RIGHT - Medium")
public class RightMedium extends CommandOpMode {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        // Subsystems
        Extension extension = new Extension(hardwareMap, telemetry);
        Rotation rotation = new Rotation(hardwareMap, telemetry);
        LimitSwitch extensionLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionSW");
        LimitSwitch rotationBottomSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationBSW");
        LimitSwitch rotationTopSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationTSW");
        Claw claw = new Claw(hardwareMap, telemetry);

        BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Creating Paths");
        telemetry.update();

        Pose2d start = new Pose2d(-33.0, 64, Math.toRadians(270));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, 13.375))
                .splineTo(new Vector2d(-36, 40), Math.toRadians(270))
                .lineToSplineHeading(new Pose2d(-35, 11, Math.toRadians(135)))
                .strafeRight(0.6)
                .back(8)
                .build();

        TrajectorySequence pickUpPosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, 13.375))
                .splineTo(new Vector2d(-40, 12), Math.toRadians(180))
                .lineTo(new Vector2d(-62, 12))
                .build();

        TrajectorySequence approachPole = drive.trajectorySequenceBuilder(pickUpPosition.end())
                .setReversed(true)
                .lineTo(new Vector2d(-50, 11))
                .lineToSplineHeading(new Pose2d(-35, 11, Math.toRadians(135)))
                .strafeRight(0.6)
                .back(8)
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

        register(extensionLimitSwitch, extension, claw, rotation, rotationBottomSwitch, rotationTopSwitch);

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(baseWebcam::stop),
                        new CloseClaw(claw),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new SequentialCommandGroup(
                                        new WaitCommand(350),
                                        new HighGoal(rotation, rotationBottomSwitch, rotationTopSwitch, extension, extensionLimitSwitch, claw)
                                )
                        ),
                        new OpenClaw(claw),
                        new Substation(rotation, extension, extensionLimitSwitch, rotationBottomSwitch, rotationBottomSwitch, claw),
                        new ParallelCommandGroup(
                                new Extend(extension, extensionLimitSwitch, ConeStackLevel.FIVE),
                                new FollowTrajectoryCommand(drive, pickUpPosition)
                        ),
                        new CloseClaw(claw),
                        new WaitCommand(250),
                        new ParallelCommandGroup(
                                new HighGoal(rotation, rotationBottomSwitch, rotationTopSwitch, extension, extensionLimitSwitch, claw),
                                new FollowTrajectoryCommand(drive, approachPole)
                        ),
                        new OpenClaw(claw),

                        new InstantCommand(() -> logger.info("Finished Program"))
                )
        );

    }
}
