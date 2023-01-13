package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class Rotate extends CommandBase {
    private final Rotation primaryRotation;

    private final int targetAngle;
    private final double speed;

    public Rotate(Rotation rotation, int targetAngle, double speed) {
        addRequirements(rotation);

        this.primaryRotation = rotation;
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