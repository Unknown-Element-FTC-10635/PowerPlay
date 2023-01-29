package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.util.color.TapeMeasureColor;

import java.lang.annotation.Target;
import java.util.logging.Logger;

public class Extend extends CommandBase {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Extension extension;
    private final Extension.TargetLevel level;

    public Extend(Extension extension, Extension.TargetLevel level) {
        this.extension = extension;
        this.level = level;

    }

    @Override
    public void initialize() {
        extension.setTargetLevel(level);

        logger.info("Initializing Extension " + level);
    }

    @Override
    public boolean isFinished() {
        logger.info("Finished Extension from Position");
        return extension.atTargetLevel();

    }
}
