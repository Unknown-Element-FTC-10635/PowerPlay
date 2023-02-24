package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Claw;

public class CloseClaw extends CommandBase {
    Claw claw;

    public CloseClaw(Claw claw) {
        this.claw = claw;
    }

    @Override
    public void initialize() {
        claw.close();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
