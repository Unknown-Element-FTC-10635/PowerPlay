package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;

public class RotatePrimary extends CommandBase {
    private final PrimaryRotation primaryRotation;

    private final int targetAngle;
    private final double speed;

    public RotatePrimary(PrimaryRotation primaryRotation, int targetAngle, double speed) {
        addRequirements(primaryRotation);

        this.primaryRotation = primaryRotation;
        this.targetAngle = targetAngle;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        primaryRotation.rotateTo(targetAngle, speed);
    }

    @Override
    public boolean isFinished() {
        return primaryRotation.atTargetPosition();
    }

    @Override
    public void end(boolean interrupted) {
        primaryRotation.stop();
    }
}