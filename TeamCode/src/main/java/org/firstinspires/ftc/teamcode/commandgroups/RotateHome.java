package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

import java.util.logging.Logger;

public class RotateHome extends SequentialCommandGroup {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public RotateHome(Rotation rotation, LimitSwitch limitSwitch) {
        logger.info("Scheduling Rotate Home");
        addCommands(
                //new Rotate(rotation, limitSwitch, 10, 0.005),
                //new WaitCommand(200),
                new Rotate(rotation, limitSwitch, 1, 0.005)
        );
    }
}
