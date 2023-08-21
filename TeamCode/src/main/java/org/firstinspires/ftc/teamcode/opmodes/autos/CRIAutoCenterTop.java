package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.PurePursuitCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.HighGoal;
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
import org.firstinspires.ftc.teamcode.util.lift.LiftHeight;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

import java.util.logging.Logger;

@Autonomous(name = "Center Auto (Top) - CRI")
public class CRIAutoCenterTop extends CommandOpMode {
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

        Pose2d start = new Pose2d(-8, -64, Math.toRadians(90));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(60, 60, DriveConstants.TRACK_WIDTH))
                .splineTo(new Vector2d(13, -18), Math.toRadians(90))
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .lineToLinearHeading(new Pose2d(13, -12, Math.toRadians(-45)))
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .waitSeconds(0.25)
                .back(8)
                .build();

        TrajectorySequence setUpPark = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .splineTo(new Vector2d(13, -15), Math.toRadians(270))
                .build();

        TrajectorySequence signalZone1 = drive.trajectorySequenceBuilder(setUpPark.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .setReversed(true)
                .lineTo(new Vector2d(13, 12))
                .build();

        TrajectorySequence signalZone2 = drive.trajectorySequenceBuilder(setUpPark.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .setReversed(true)
                .lineTo(new Vector2d(13, 13))
                .turn(Math.toRadians(-90))
                .lineTo(new Vector2d(-33, 12))
                .build();

        TrajectorySequence signalZone3 = drive.trajectorySequenceBuilder(setUpPark.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .setReversed(true)
                .lineTo(new Vector2d(13, 13))
                .turn(Math.toRadians(-90))
                .lineTo(new Vector2d(-10, 12))
                .build();

        telemetry.addLine("Starting Webcam");
        telemetry.update();

        baseWebcam.startSleeveDetection(false);

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
                        new WaitCommand(500),
                        new Extend(extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, ConeStackLevel.THREE),
                        new FollowTrajectoryCommand(drive, preloadDelivery),
                        new HighGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, claw),
                        new WaitCommand(900),
                        new OpenClawDeliver(claw),
                        new WaitCommand(100),
                        // -- PRELOAD --

                        new Substation(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw),
                        new FollowTrajectoryCommand(drive, setUpPark),
                        new OpenClawPickUp(claw),
                        new PickPark(drive, sleeveColor, signalZone1, signalZone2, signalZone3),

                        new InstantCommand(() -> logger.info("Finished Program"))
                )
        );

    }
}
