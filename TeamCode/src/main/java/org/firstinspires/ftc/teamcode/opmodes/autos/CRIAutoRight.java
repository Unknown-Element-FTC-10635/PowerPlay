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
import org.firstinspires.ftc.teamcode.commandgroups.PickUpStack;
import org.firstinspires.ftc.teamcode.commandgroups.Substation;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.OpenClawDeliver;
import org.firstinspires.ftc.teamcode.commands.OpenClawPickUp;
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

@Autonomous(name = "Right Auto - CRI")
public class CRIAutoRight extends CommandOpMode {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

        CurrentOpmode.setCurrentOpmode(CurrentOpmode.OpMode.AUTO);

        // Subsystems
        LimitSwitch extensionLeftLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionLSW");
        LimitSwitch extensionRightLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionRSW");
        LimitSwitch rotationBottomLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationBSW");
        LimitSwitch rotationTopLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationTSW");
        Extension extension = new Extension(hardwareMap, telemetry);
        Rotation rotation = new Rotation(hardwareMap, telemetry);
        Claw claw = new Claw(hardwareMap, telemetry);

        extensionLeftLimitSwitch.setInverted(true);

        BaseWebcam baseWebcam = new BaseWebcam(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Creating Paths");
        telemetry.update();

        Pose2d start = new Pose2d(-33.0, 64, Math.toRadians(270));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .splineTo(new Vector2d(-36, 40), Math.toRadians(270))
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(100, 100, DriveConstants.TRACK_WIDTH))
                .lineTo(new Vector2d(-35, 5))
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .back(8)
                .lineToSplineHeading(new Pose2d(-37, 12, Math.toRadians(225)))
                .back(12)
                .build();

        TrajectorySequence pickUpStackPosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(50, 50, DriveConstants.TRACK_WIDTH))
                .splineTo(new Vector2d(-40, 13), Math.toRadians(180))
                .lineTo(new Vector2d(-60, 13))
                .forward(0.5)
                .build();

        TrajectorySequence approachPole = drive.trajectorySequenceBuilder(pickUpStackPosition.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .setReversed(true)
                .lineTo(new Vector2d(-40, 12))
                .splineTo(new Vector2d(-28, 18), Math.toRadians(45))
                .build();

        TrajectorySequence setUpPark = drive.trajectorySequenceBuilder(approachPole.end())
                .splineTo(new Vector2d(-40, 12), Math.toRadians(180))
                .build();

        TrajectorySequence signalZone1 = drive.trajectorySequenceBuilder(setUpPark.end())
                .lineTo(new Vector2d(12, 13))
                .build();

        TrajectorySequence signalZone2 = drive.trajectorySequenceBuilder(setUpPark.end())
                .lineTo(new Vector2d(-8, 12))
                .build();

        TrajectorySequence signalZone3 = drive.trajectorySequenceBuilder(setUpPark.end())
                .back(6)
                .build();

        telemetry.addLine("Starting Webcam");
        telemetry.update();

        baseWebcam.startSleeveDetection(true);

        telemetry.addLine("Ready to Start");
        telemetry.update();


        waitForStart();
        logger.info("Starting Match");


        SleeveDetection.SleeveColor sleeveColor = baseWebcam.getSleeveColor();
        telemetry.addData("Found Sleeve ", sleeveColor);

        telemetry.addLine("Scheduling Tasks");
        telemetry.update();

        register(extensionLeftLimitSwitch, extensionRightLimitSwitch, extension, claw, rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch);

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(baseWebcam::stop),
                        new OpenClawDeliver(claw),
                        new CloseClaw(claw),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new SequentialCommandGroup(
                                        new WaitCommand(600),
                                        new MediumGoal(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw))
                        ),
                        new WaitCommand(250),
                        new OpenClawDeliver(claw),
                        new WaitCommand(100),
                        // -- PRELOAD --

                        new PickUpStack(drive, extension, rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extensionLeftLimitSwitch, extensionRightLimitSwitch, claw, pickUpStackPosition, ConeStackLevel.FIVE),
                        new ParallelCommandGroup(
                                new Extend(extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, ConeStackLevel.BACK_AWAY),
                                new SequentialCommandGroup(
                                        new WaitCommand(400),
                                        new FollowTrajectoryCommand(drive, approachPole)
                                )
                        ),
                        new MediumGoal(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw),
                        new WaitCommand(200),
                        new OpenClawDeliver(claw),
                        new WaitCommand(150),
                        // -- CYCLE 1 --

                        new PickUpStack(drive, extension, rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extensionLeftLimitSwitch, extensionRightLimitSwitch, claw, pickUpStackPosition, ConeStackLevel.FIVE),
                        new ParallelCommandGroup(
                                new Extend(extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, ConeStackLevel.BACK_AWAY),
                                new SequentialCommandGroup(
                                        new WaitCommand(400),
                                        new FollowTrajectoryCommand(drive, approachPole)
                                )
                        ),
                        new MediumGoal(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw),
                        new WaitCommand(200),
                        new OpenClawDeliver(claw),
                        new WaitCommand(150),
                        // -- CYCLE 2 --

                        new Substation(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw),
                        new FollowTrajectoryCommand(drive, setUpPark),
                        new OpenClawPickUp(claw),
                        new PickPark(drive, sleeveColor, signalZone1, signalZone2, signalZone3),

                        new InstantCommand(() -> logger.info("Finished Program"))
                )
        );

    }
}
