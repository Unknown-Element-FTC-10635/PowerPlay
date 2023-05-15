package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.RotateTop;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.lift.PoleLevel;

public class HighGoal extends ParallelCommandGroup {
    public HighGoal(Rotation rotation, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Extension extension, LimitSwitch extensionLeftSwitch, LimitSwitch extensionRightSwitch, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new Extend(extension, extensionLeftSwitch, extensionRightSwitch, PoleLevel.HIGH),
                new SequentialCommandGroup(
                        new WaitCommand(200),
                        new RotateTop(rotation, topSwitch, 1)
                )
        );
    }
}
