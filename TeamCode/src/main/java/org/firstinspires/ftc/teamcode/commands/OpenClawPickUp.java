package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Claw;

public class OpenClawPickUp extends CommandBase {
    Claw claw;

    public OpenClawPickUp(Claw claw) {
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
