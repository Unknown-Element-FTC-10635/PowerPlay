package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Claw;

public class OpenClawDeliver extends CommandBase {
    Claw claw;

    public OpenClawDeliver(Claw claw) {
        this.claw = claw;
    }

    @Override
    public void initialize() {
        claw.openSmall();
    }

    @Override
    public boolean isFinished() {
        return (claw.getCurrentState() == Claw.State.OPEN);
    }
}
