package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.TertiaryRotation;

public class RotateTertiary extends CommandBase {
    TertiaryRotation tertiaryRotation;

    double position;

    public RotateTertiary(TertiaryRotation tertiaryRotation, double position) {
        addRequirements(tertiaryRotation);

        this.tertiaryRotation = tertiaryRotation;
        this.position = position;
    }

    @Override
    public void initialize() {
        tertiaryRotation.goToPosition(position);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
