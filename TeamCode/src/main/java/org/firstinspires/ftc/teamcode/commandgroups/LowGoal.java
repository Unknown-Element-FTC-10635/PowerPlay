package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.RotateTop;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class LowGoal extends ParallelCommandGroup {
    public LowGoal(Rotation rotation, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new RotateTop(rotation, topSwitch,1)
        );
    }
}
