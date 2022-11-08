package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;

public class RetractSlides extends CommandBase {
    private final Extension extension;

    private final double inches, speed;

    public RetractSlides(Extension extension, double inches, double speed) {
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
        return extension.atTargetRetraction();
    }

    @Override
    public void end(boolean interrupted) {
        extension.stop();
    }
}
