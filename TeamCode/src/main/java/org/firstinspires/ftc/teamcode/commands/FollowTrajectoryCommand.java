package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class FollowTrajectoryCommand extends CommandBase {
    private final SampleMecanumDrive drive;
    private final TrajectorySequence trajectorySequence;

    public FollowTrajectoryCommand(SampleMecanumDrive drive, TrajectorySequence trajectory) {
        this.drive = drive;
        trajectorySequence = trajectory;
    }

    @Override
    public void initialize() {
        drive.followTrajectorySequenceAsync(trajectorySequence);
    }

    @Override
    public void execute() {
        drive.update();
    }

    @Override
    public boolean isFinished() {
        return !drive.isBusy();
    }
}