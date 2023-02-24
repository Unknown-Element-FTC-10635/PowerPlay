package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.RotateTop;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.lift.PoleLevel;

public class MediumGoal extends ParallelCommandGroup {
    public MediumGoal(Rotation rotation, Extension extension, LimitSwitch extensionLeftSwitch, LimitSwitch extensionRightSwitch, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new RotateTop(rotation, topSwitch, 1),
                new Extend(extension, extensionLeftSwitch, extensionRightSwitch, PoleLevel.MEDIUM)
        );
    }
}
