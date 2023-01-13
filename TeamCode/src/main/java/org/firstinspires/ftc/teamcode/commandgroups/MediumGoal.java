package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class MediumGoal extends ParallelCommandGroup {
    public MediumGoal(Rotation rotation, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new Rotate(rotation,105, 0.01)
        );
    }
}
