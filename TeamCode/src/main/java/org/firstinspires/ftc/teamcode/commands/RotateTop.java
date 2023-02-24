package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

import java.util.logging.Logger;

public class RotateTop extends CommandBase {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Rotation primaryRotation;
    private final LimitSwitch topSwitch;

    private final double speed;

    public RotateTop(Rotation rotation, LimitSwitch topSwitch, double speed) {
        addRequirements(rotation, topSwitch);

        this.primaryRotation = rotation;
        this.topSwitch = topSwitch;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        primaryRotation.rotateTo(true, speed);
        logger.info("Initializing Rotation " + speed);
    }

    @Override
    public boolean isFinished() {
        if (topSwitch.isPressed()) {
            logger.info("Finished Rotation from Top Limit Switch");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        primaryRotation.stop();
        logger.info("Ending at current angle: " + primaryRotation.getAngle());
    }
}