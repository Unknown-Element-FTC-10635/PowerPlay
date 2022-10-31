package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.RotatePrimary;
import org.firstinspires.ftc.teamcode.commands.RotateSecondary;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.SecondaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;

public class UnfoldArm extends SequentialCommandGroup {
    public UnfoldArm(PrimaryRotation primaryRotation, SecondaryRotation secondaryRotation, TertiaryRotation tertiaryRotation) {
        addCommands(
                new ParallelCommandGroup(
                        new RotatePrimary(primaryRotation, -135, 0.75),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new ParallelCommandGroup(
                                        new RotateSecondary(secondaryRotation, 0.1),
                                        new InstantCommand(tertiaryRotation::goToInitialPosition)
                                )
                        )
                ),
                new ParallelCommandGroup(
                        new RotatePrimary(primaryRotation, -20, 0.5),
                        new RotateSecondary(secondaryRotation, 0.4)
                )
        );
        addRequirements(primaryRotation, secondaryRotation, tertiaryRotation);
    }
}
