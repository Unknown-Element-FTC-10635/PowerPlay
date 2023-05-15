package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

import java.util.logging.Logger;

public class RotateZero extends CommandBase {
    //private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Rotation primaryRotation;
    private final LimitSwitch bottomSwitch;

    private final double speed;

    public RotateZero(Rotation rotation, LimitSwitch bottomSwitch, double speed) {
        addRequirements(rotation, bottomSwitch);

        this.primaryRotation = rotation;
        this.bottomSwitch = bottomSwitch;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        primaryRotation.rotateTo(false, speed);
        //logger.info("Initializing Rotation " + speed);
    }

    @Override
    public boolean isFinished() {
        if (bottomSwitch.isPressed() && primaryRotation.getAngle() < 20) {
            //logger.info("Finished Rotation from Bottom Limit Switch");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        primaryRotation.stop();
        primaryRotation.reset();
        //logger.info("Ending at current angle: " + primaryRotation.getAngle());
    }
}