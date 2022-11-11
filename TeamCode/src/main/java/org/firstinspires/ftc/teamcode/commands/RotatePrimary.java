package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;

public class RotatePrimary extends CommandBase {
    private final PrimaryRotation primaryRotation;
    private final Extension extension;

    private final int targetAngle;
    private final double speed;

    public RotatePrimary(PrimaryRotation primaryRotation, Extension extension, int targetAngle, double speed) {
        addRequirements(primaryRotation, extension);

        this.primaryRotation = primaryRotation;
        this.extension = extension;
        this.targetAngle = targetAngle;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        primaryRotation.rotateTo(targetAngle, speed);
        extension.rotatePower((float)-0.05);
    }

    @Override
    public boolean isFinished() {
        return primaryRotation.atTargetPosition();
    }

    @Override
    public void end(boolean interrupted) {
        primaryRotation.stop();
        extension.stop();
    }
}
