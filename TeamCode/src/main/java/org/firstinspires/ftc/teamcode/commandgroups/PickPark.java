package org.firstinspires.ftc.teamcode.commandgroups;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;

public class PickPark extends SequentialCommandGroup {
    public PickPark(SampleMecanumDrive drive, SleeveDetection.SleeveColor color, TrajectorySequence purple, TrajectorySequence orange, TrajectorySequence green) {
        switch (color) {
            case PURPLE:
                addCommands(new FollowTrajectoryCommand(drive, purple));
                break;
            case ORANGE:
                addCommands(new FollowTrajectoryCommand(drive, orange));
                break;
            case GREEN:
                addCommands(new FollowTrajectoryCommand(drive, green));
                break;
        }
    }
}