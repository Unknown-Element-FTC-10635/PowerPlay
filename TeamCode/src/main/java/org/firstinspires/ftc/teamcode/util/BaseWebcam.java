package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.visionpipeline.ConeStackDetection;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection;
import org.firstinspires.ftc.teamcode.visionpipeline.SleeveDetection.SleeveColor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.logging.Logger;

public class BaseWebcam {
    private final HardwareMap hardwareMap;
    private final Logger logger = Logger.getLogger(BaseWebcam.class.getName());
    private OpenCvWebcam webcam;

    private SleeveDetection sleeveDetection;
    private ConeStackDetection coneStackDetection;

    public BaseWebcam(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void startSleeveDetection(boolean isRight) {
        sleeveDetection = new SleeveDetection(isRight);
        start(sleeveDetection);
    }

    public SleeveColor getSleeveColor() {
        return sleeveDetection.getSleeveColor();
    }

    public void startStackDetectionBlue() {
        coneStackDetection = new ConeStackDetection();
        coneStackDetection.setProcessBlue(true);
        start(coneStackDetection);
    }

    public void startStackDetectionRed() {
        coneStackDetection = new ConeStackDetection();
        coneStackDetection.setProcessBlue(false);
        start(coneStackDetection);
    }

    public ConeStackDetection.StackDirections getStackDirection() {
       return coneStackDetection.getDirection();
    }

    public ConeStackDetection.StackEstimate getFrameEstimate() {
        return coneStackDetection.getFrameEstimate();
    }

    public double getStackDistance() {
        return coneStackDetection.getDistance();
    }

    private void start(OpenCvPipeline pipeline) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Base"), cameraMonitorViewId);

        webcam.setPipeline(pipeline);

        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.

        logger.info("Opening Camera Device");
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                logger.info("Calling StartStreaming");
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                logger.warning("EasyOpenCV error: " + errorCode);
            }
        });

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.startCameraStream(webcam, 30);
    }

    public void stop() {
        webcam.stopStreaming();
    }

    public void switchPipelineConeStack() {
        coneStackDetection = new ConeStackDetection();
        webcam.setPipeline(coneStackDetection);
    }
}
