package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.BaseWebcam;
import org.firstinspires.ftc.teamcode.visionpipeline.ConeStackDetection;

public class FaceConeStack extends CommandBase {
    private SampleMecanumDrive drive;
    private BaseWebcam webcam;

    private final double SPEED = 0.15;

    public FaceConeStack(SampleMecanumDrive drive, BaseWebcam webcam) {
        this.drive = drive;
        this.webcam = webcam;
    }

    @Override
    public void execute() {
        if (webcam.getStackDirection() == ConeStackDetection.StackDirections.CENTER) {
            drive.setMotorPowers(0.25, 0.25, 0.25, 0.25);
        }else if (webcam.getStackDirection() == ConeStackDetection.StackDirections.LEFT) {
            drive.setMotorPowers(SPEED, SPEED, -SPEED, -SPEED);
        } else if (webcam.getStackDirection() == ConeStackDetection.StackDirections.RIGHT) {
            drive.setMotorPowers(-SPEED, -SPEED, SPEED, SPEED);
        }
        drive.update();
    }

    @Override
    public void end(boolean interrupted) {
        drive.setMotorPowers(0, 0, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return (webcam.getFrameEstimate() == ConeStackDetection.StackEstimate.FILLED && webcam.getStackDirection() == ConeStackDetection.StackDirections.CENTER);
    }
}
