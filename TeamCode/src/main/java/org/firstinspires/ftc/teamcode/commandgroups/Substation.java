package org.firstinspires.ftc.teamcode.commandgroups;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.Limbo;
import org.firstinspires.ftc.teamcode.commands.OpenClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class Substation extends SequentialCommandGroup {
    public Substation(Rotation rotation, Extension extension, LimitSwitch extensionLimitSwitch, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Claw claw) {
        addCommands(
                new ParallelRaceGroup(
                        new OpenClaw(claw),
                        new Limbo(extension, extensionLimitSwitch),
                        new Rotate(rotation, bottomSwitch, topSwitch, 15, 0.2)
                ),
                new Rotate(rotation, bottomSwitch, topSwitch, 0, .15)
        );
    }
}
