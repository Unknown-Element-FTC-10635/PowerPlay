package org.firstinspires.ftc.teamcode.commandgroups;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.OpenClaw;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.lift.PoleLevel;

public class Substation extends SequentialCommandGroup {
    public Substation(Rotation rotation, Extension extension, LimitSwitch extensionLimitSwitch, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Claw claw) {
        addCommands(
                new ParallelCommandGroup(
                        new OpenClaw(claw),
                        new Extend(extension, extensionLimitSwitch, PoleLevel.SUBSTATION),
                        new Rotate(rotation, bottomSwitch, topSwitch, 30, 0.2)
                ),
                new RotateHome(rotation, bottomSwitch, topSwitch)
        );
    }
}
