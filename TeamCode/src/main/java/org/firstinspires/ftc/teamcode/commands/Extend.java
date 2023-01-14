package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;

public class Extend extends CommandBase {
    private final Extension extension;
    private final double inches;
    private final double speed;

    public Extend(Extension extension, double inches, double speed) {
        this.extension = extension;
        this.inches = inches;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        extension.rotateInches(inches, speed);
    }

    @Override
    public void end(boolean interrupted) {
        extension.stop();
    }

    @Override
    public boolean isFinished() {
        return extension.atTargetExtension();
    }
}
