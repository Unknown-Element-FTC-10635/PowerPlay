package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.commands.RotateZero;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

import java.util.logging.Logger;

public class RotateHome extends SequentialCommandGroup {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public RotateHome(Rotation rotation, LimitSwitch bottomSwitch, LimitSwitch topSwitch) {
        logger.info("Scheduling Rotate Home");
        addCommands(
                new Rotate(rotation, bottomSwitch, topSwitch, 80, -1),
                new Rotate(rotation, bottomSwitch, topSwitch, 40, -0.3),
                new RotateZero(rotation, bottomSwitch,0.4)
        );
    }
}
