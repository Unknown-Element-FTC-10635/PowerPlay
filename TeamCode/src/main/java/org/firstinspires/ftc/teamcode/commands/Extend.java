package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.util.lift.LiftHeight;

import java.util.logging.Logger;

public class Extend extends CommandBase {
    //private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Extension extension;
    private final LimitSwitch extensionLeftSwitch, extensionRightSwitch;
    private final LiftHeight level;

    public Extend(Extension extension, LimitSwitch extensionLeftSwitch, LimitSwitch extensionRightSwitch, LiftHeight level) {
        this.extension = extension;
        this.extensionLeftSwitch = extensionLeftSwitch;
        this.extensionRightSwitch = extensionRightSwitch;
        this.level = level;

    }

    @Override
    public void initialize() {
        extension.setTargetLevel(level);

        //logger.info("Initializing Extension " + level);
    }

    @Override
    public boolean isFinished() {
        if (extension.atTargetLevel()) {
            //logger.info("Finished Extension from Position");
            return true;
        } else if (extensionLeftSwitch.isPressed()) {
            //logger.info("Finished Extension from Left Limit Switch");
            return true;
        } else if (extensionRightSwitch.isPressed()) {
            //logger.info("Finished Extension from Right Limit Switch");
            return true;
        } else {
            return false;
        }
    }
}
