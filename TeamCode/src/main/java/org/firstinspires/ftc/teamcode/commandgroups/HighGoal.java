package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class HighGoal extends ParallelCommandGroup {
    public HighGoal(Rotation rotation, LimitSwitch limitSwitch, Extension extension, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new Rotate(rotation, limitSwitch, 100, 0.35),
                new Extend(extension,15, 1)
        );
    }
}
