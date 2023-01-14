package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

import java.util.logging.Logger;

public class Rotate extends CommandBase {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Rotation primaryRotation;
    private final LimitSwitch limitSwitch;

    private final int targetAngle;
    private final double speed;

    public Rotate(Rotation rotation, LimitSwitch limitSwitch, int targetAngle, double speed) {
        addRequirements(rotation, limitSwitch);

        this.primaryRotation = rotation;
        this.limitSwitch = limitSwitch;
        this.targetAngle = targetAngle;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        primaryRotation.rotateTo(targetAngle, speed);
        logger.info("Initializing Rotation " + targetAngle + ", " + speed + ", " + primaryRotation.getAngle());
    }

    @Override
    public boolean isFinished() {
        if (primaryRotation.atTargetPosition()) {
            logger.info("Finished Rotation from atTargetPosition()");
            return true;
        } else if (!primaryRotation.isGoingUp() && limitSwitch.isPressed()) {
            logger.info("Finished Rotation from Limit Switch");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        primaryRotation.stop();
        if (limitSwitch.isPressed()) {
            primaryRotation.reset();
        }
        logger.info("Ending at current angle: " + primaryRotation.getAngle());
    }
}