package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class LowGoal extends ParallelCommandGroup {
    public LowGoal(Rotation rotation, LimitSwitch limitSwitch, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new Rotate(rotation, limitSwitch,50, 0.35)
        );
    }
}
