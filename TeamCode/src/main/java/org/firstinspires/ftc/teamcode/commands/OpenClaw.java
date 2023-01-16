package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Claw;

public class OpenClaw extends CommandBase {
    Claw claw;

    public OpenClaw(Claw claw) {
        this.claw = claw;
    }

    @Override
    public void initialize() {
        claw.openBig();
    }

    @Override
    public boolean isFinished() {
        return (claw.getCurrentState() == Claw.State.OPEN);
    }
}
