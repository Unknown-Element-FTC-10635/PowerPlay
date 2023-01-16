package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.commands.CloseClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class MediumGoal extends ParallelCommandGroup {
    public MediumGoal(Rotation rotation, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Claw claw) {
        addCommands(
                new CloseClaw(claw),
                new Rotate(rotation, bottomSwitch, topSwitch, 125, 0.01)
        );
    }
}
