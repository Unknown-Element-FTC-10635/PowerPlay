package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.FlipPrimary;
import org.firstinspires.ftc.teamcode.commands.RotateTertiary;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;
import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;

public class UnfoldArm extends SequentialCommandGroup {
    public UnfoldArm(PrimaryRotation primaryRotation, TertiaryRotation tertiaryRotation, Extension extension, LimitSwitch limitSwitch) {
        addCommands(
                new ParallelCommandGroup(
                        new FlipPrimary(primaryRotation, extension, limitSwitch),
                        new RotateTertiary(tertiaryRotation, 0.45)
                ),
                new InstantCommand(() -> tertiaryRotation.setBeginAdjustment(true))
        );
        addRequirements(primaryRotation, tertiaryRotation, extension, limitSwitch);
    }
}
