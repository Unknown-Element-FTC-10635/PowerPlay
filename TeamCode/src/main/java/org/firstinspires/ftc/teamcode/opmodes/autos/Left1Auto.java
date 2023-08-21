package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.commandgroups.HighGoal;
import org.firstinspires.ftc.teamcode.commandgroups.PickPark;
import org.firstinspires.ftc.teamcode.commandgroups.Substation;
import org.firstinspires.ftc.teamcode.commands.CloseClaw;
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
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

import java.util.logging.Logger;

@Disabled
@Autonomous(name = "LEFT (1+0) - High", group = "Left")
public class Left1Auto extends CommandOpMode {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void initialize() {
        telemetry.addLine("Creating Subsystems");
        telemetry.update();

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

        Pose2d start = new Pose2d(-33.0, -64, Math.toRadians(90));
        drive.setPoseEstimate(start);

        TrajectorySequence preloadDelivery = drive.trajectorySequenceBuilder(start)
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, DriveConstants.TRACK_WIDTH))
                .splineTo(new Vector2d(-36, -40), Math.toRadians(90))
                .lineToSplineHeading(new Pose2d(-37, -15.75, Math.toRadians(225)))
                .back(11.5)
                .build();

        TrajectorySequence setUpPark = drive.trajectorySequenceBuilder(preloadDelivery.end())
                .splineTo(new Vector2d(-40, -12), Math.toRadians(180))
                .build();

        TrajectorySequence orange = drive.trajectorySequenceBuilder(setUpPark.end())
                .back(6)
                .strafeLeft(5)
                .build();

        TrajectorySequence purple = drive.trajectorySequenceBuilder(setUpPark.end())
                .lineTo(new Vector2d(-60, -16))
                .build();

        TrajectorySequence green = drive.trajectorySequenceBuilder(setUpPark.end())
                .lineTo(new Vector2d(-10, -16))
                .strafeLeft(5)
                .build();

        telemetry.addLine("Starting Webcam");
        telemetry.update();

        baseWebcam.startSleeveDetection(false);

        telemetry.addLine("Ready to Start");
        telemetry.update();

        CurrentOpmode.setCurrentOpmode(CurrentOpmode.OpMode.AUTO);

        waitForStart();

        logger.info("Starting Match");

        SleeveDetection.SleeveColor sleeveColor = baseWebcam.getSleeveColor();
        telemetry.addData("Found Sleeve ", sleeveColor);

        telemetry.addLine("Scheduling Tasks");
        telemetry.update();

        register(extensionLeftLimitSwitch, extension, claw, rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch);

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
                                        new HighGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, claw)
                                )
                        ),
                        new OpenClawDeliver(claw),
                        new WaitCommand(1000),
                        new Substation(rotation, extension, extensionLeftLimitSwitch, extensionRightLimitSwitch, rotationBottomLimitSwitch, rotationTopLimitSwitch, claw),
                        new FollowTrajectoryCommand(drive, setUpPark),
                        new OpenClawPickUp(claw),
                        new PickPark(drive, sleeveColor, purple, orange, green),

                        new InstantCommand(() -> logger.info("Finished Program"))
                )
        );

    }
}
