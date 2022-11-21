package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.RunCommand;

import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.PrimaryRotation;

public class FlipPrimary extends CommandBase {
    private final PrimaryRotation primaryRotation;
    private final Extension extension;
    private final LimitSwitch limitSwitch;

    public FlipPrimary(PrimaryRotation primaryRotation, Extension extension, LimitSwitch limitSwitch) {
        addRequirements(primaryRotation, limitSwitch);

        this.primaryRotation = primaryRotation;
        this.extension = extension;
        this.limitSwitch = limitSwitch;
    }

    @Override
    public void initialize() {
        primaryRotation.rotatePower((float)-0.5);
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return limitSwitch.isPressed();
    }

    @Override
    public void end(boolean interrupted) {
        primaryRotation.stop();
        primaryRotation.reset();
    }
}
