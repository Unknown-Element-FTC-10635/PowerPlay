package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.util.color.TapeMeasureColor;

public class Extend extends CommandBase {
    private final Extension extension;
    private final TapeMeasureColor color;
    private final double speed;

    public Extend(Extension extension, TapeMeasureColor color, double speed) {
        this.extension = extension;
        this.color = color;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        extension.rotateLevel(color, speed);
    }

    @Override
    public void end(boolean interrupted) {
        extension.stop();
        extension.reset();
    }

    @Override
    public boolean isFinished() {
        return extension.atTargetLevel();
    }
}
