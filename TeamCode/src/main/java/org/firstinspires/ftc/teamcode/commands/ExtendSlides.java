package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;

public class ExtendSlides extends CommandBase {
    private final Extension extension;

    private final double inches, speed;

    public ExtendSlides(Extension extension, double inches, double speed) {
        addRequirements(extension);

        this.extension = extension;
        this.inches = inches;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        extension.rotateInches(inches, speed);
    }

    @Override
    public boolean isFinished() {
        return extension.atTargetExtension();
    }

    @Override
    public void end(boolean interrupted) {
        extension.stop();
    }
}
