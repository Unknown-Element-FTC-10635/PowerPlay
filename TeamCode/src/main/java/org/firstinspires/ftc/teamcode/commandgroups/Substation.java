package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.Extend;
import org.firstinspires.ftc.teamcode.commands.OpenClawDeliver;
import org.firstinspires.ftc.teamcode.commands.RotateZero;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.lift.PoleLevel;

public class Substation extends ParallelCommandGroup {
    public Substation(Rotation rotation, Extension extension, LimitSwitch extensionLeftSwitch, LimitSwitch extensionRightSwitch, LimitSwitch bottomSwitch, LimitSwitch topSwitch, Claw claw) {
        addCommands(
                new OpenClawDeliver(claw),
                new RotateHome(rotation, bottomSwitch, topSwitch),
                new SequentialCommandGroup(
                        new WaitCommand(250),
                        new Extend(extension, extensionLeftSwitch, extensionRightSwitch, PoleLevel.SUBSTATION)
                )
        );
    }
}
