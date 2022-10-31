package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.SecondaryRotation;

public class RotateSecondary extends CommandBase {
    private final SecondaryRotation secondaryRotation;

    private final double targetAngle;

    public RotateSecondary(SecondaryRotation secondaryRotation, double targetAngle) {
        addRequirements(secondaryRotation);

        this.secondaryRotation = secondaryRotation;
        this.targetAngle = targetAngle;
    }

    @Override
    public void initialize() {
        secondaryRotation.rotateTo(targetAngle);
    }

    @Override
    public boolean isFinished() {
        return (secondaryRotation.getAngle() == targetAngle);
    }
}
