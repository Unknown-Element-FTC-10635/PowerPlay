package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

public class Limbo extends CommandBase {
    private final Extension extension;
    private final LimitSwitch limitSwitch;

    public Limbo(Extension extension, LimitSwitch limitSwitch) {
        this.extension = extension;
        this.limitSwitch = limitSwitch;
    }

    @Override
    public void initialize() {
        extension.setTargetLevel(Extension.TargetLevel.SUBSTATION);
    }

    @Override
    public void end(boolean interrupted) {
        if (limitSwitch.isPressed()) {
            extension.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return limitSwitch.isPressed() || extension.atTargetLevel();
    }
}
