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
import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commandgroups.PickUpStack;
import org.firstinspires.ftc.teamcode.commandgroups.Substation;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.OpenClawDeliver;
import org.firstinspires.ftc.teamcode.commands.OpenClawPickUp;
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

@Autonomous(name = "RIGHT (1+2) - High", group = "Right")
public class Right12Auto extends CommandOpMode {
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
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .splineTo(new Vector2d(-36, 40), Math.toRadians(270))
                .lineToSplineHeading(new Pose2d(-36.5, 12, Math.toRadians(135)))
                .back(10)
                .build();

        TrajectorySequence pickUpStackPosition = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(30, 30, DriveConstants.TRACK_WIDTH))
                .splineTo(new Vector2d(-40, 13), Math.toRadians(180))
                .lineTo(new Vector2d(-57.25, 13))
                .build();

        TrajectorySequence approachPole = drive.trajectorySequenceBuilder(pickUpStackPosition.end())
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(50, 50, DriveConstants.TRACK_WIDTH))
                .setReversed(true)
                .lineTo(new Vector2d(-50, 11))
                .lineToSplineHeading(new Pose2d(-36.5, 12, Math.toRadians(130)))
                .back(10)
                .build();

        TrajectorySequence setUpPark = drive.trajectorySequenceBuilder(approachPole.end())
                .splineTo(new Vector2d(-40, 12), Math.toRadians(180))
                .build();

        TrajectorySequence orange = drive.trajectorySequenceBuilder(setUpPark.end())
                .strafeRight(5)
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(setUpPark.end())
                .lineTo(new Vector2d(-10, 16))
                .strafeRight(5)
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(setUpPark.end())
                .lineTo(new Vector2d(-60, 17))
                .strafeRight(5)
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

        register(extensionLimitSwitch, extension, claw, rotation, rotationBottomSwitch, rotationTopSwitch);

        schedule(
                new SequentialCommandGroup(
                        new InstantCommand(baseWebcam::stop),
                        new OpenClawDeliver(claw),
                        new CloseClaw(claw),
                        new WaitCommand(250),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, preloadDelivery),
                                new SequentialCommandGroup(
                                        new WaitCommand(350),
                                        new HighGoal(rotation, rotationBottomSwitch, rotationTopSwitch, extension, extensionLimitSwitch, claw)
                                )
                        ),
                        new OpenClawDeliver(claw),
                        new WaitCommand(100),
                        new Substation(rotation, extension, extensionLimitSwitch, rotationBottomSwitch, rotationTopSwitch, claw),
                        new PickUpStack(drive, extension, rotation, rotationBottomSwitch, rotationTopSwitch, extensionLimitSwitch, claw, pickUpStackPosition, ConeStackLevel.FIVE),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, approachPole),
                                new SequentialCommandGroup(
                                        new WaitCommand(100),
                                        new HighGoal(rotation, rotationBottomSwitch, rotationTopSwitch, extension, extensionLimitSwitch, claw)
                                )
                        ),
                        new OpenClawDeliver(claw),
                        new WaitCommand(300),
                        new Substation(rotation, extension, extensionLimitSwitch, rotationBottomSwitch, rotationTopSwitch, claw),
                        new PickUpStack(drive, extension, rotation, rotationBottomSwitch, rotationTopSwitch, extensionLimitSwitch, claw, pickUpStackPosition, ConeStackLevel.FOUR),
                        new CloseClaw(claw),
                        new WaitCommand(250),
                        new ParallelCommandGroup(
                                new FollowTrajectoryCommand(drive, approachPole),
                                new SequentialCommandGroup(
                                        new WaitCommand(100),
                                        new HighGoal(rotation, rotationBottomSwitch, rotationTopSwitch, extension, extensionLimitSwitch, claw)
                                )
                        ),
                        new OpenClawDeliver(claw),
                        new WaitCommand(300),
                        new Substation(rotation, extension, extensionLimitSwitch, rotationBottomSwitch, rotationTopSwitch, claw),
                        new OpenClawPickUp(claw),
                        new FollowTrajectoryCommand(drive, setUpPark),
                        new PickPark(drive, sleeveColor, purple, orange, green),

                        new InstantCommand(() -> logger.info("Finished Program"))
                )
        );

    }
}
