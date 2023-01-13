package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class RotateHome extends SequentialCommandGroup {
    public RotateHome(Rotation rotation) {
        addCommands(
                new Rotate(rotation, 10, 0.005),
                new WaitCommand(200),
                new Rotate(rotation, 1, 0.005)
        );
    }
}
