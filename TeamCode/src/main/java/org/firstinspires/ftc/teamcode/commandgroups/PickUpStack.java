package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.FollowTrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.OpenClawPickUp;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.lift.LiftHeight;

public class PickUpStack extends SequentialCommandGroup {
    public PickUpStack(SampleMecanumDrive drive, Extension extension, Rotation rotation, LimitSwitch rotationBottomSwitch, LimitSwitch rotationTopSwitch, LimitSwitch extensionLimitSwitch, Claw claw, TrajectorySequence pickUpStackPosition, LiftHeight level) {
        addCommands(
                new ParallelCommandGroup(
                        new OpenClawPickUp(claw),
                        new Rotate(rotation, rotationBottomSwitch, rotationTopSwitch, 15, 0.2),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new Extend(extension, extensionLimitSwitch, level)
                        ),
                        new FollowTrajectoryCommand(drive, pickUpStackPosition)
                ),
                new CloseClaw(claw),
                new WaitCommand(250)
        );
    }
}
