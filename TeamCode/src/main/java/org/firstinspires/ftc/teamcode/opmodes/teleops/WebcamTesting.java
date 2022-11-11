package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.BaseWebcam;

@TeleOp
public class WebcamTesting extends OpMode {
    BaseWebcam webcam;

    @Override
    public void init() {
        webcam = new BaseWebcam(hardwareMap);
        webcam.startSleeveDetection();
    }

    @Override
    public void loop() {
        telemetry.addData("Sleeve Color", webcam.getSleeveColor());
        telemetry.update();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
