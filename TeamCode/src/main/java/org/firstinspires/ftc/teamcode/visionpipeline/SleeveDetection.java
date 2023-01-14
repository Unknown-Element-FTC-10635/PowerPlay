package org.firstinspires.ftc.teamcode.visionpipeline;

import android.annotation.SuppressLint;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SleeveDetection extends OpenCvPipeline {
    private final Scalar LOWER_GREEN = new Scalar(40, 10, 10);
    private final Scalar UPPER_GREEN = new Scalar(80, 255, 255);

    private final Scalar LOWER_ORANGE = new Scalar(10, 20, 40);
    private final Scalar UPPER_ORANGE = new Scalar(35, 255, 255);

    private final Scalar LOWER_PURPLE = new Scalar(130, 20, 40);
    private final Scalar UPPER_PURPLE = new Scalar(160, 255, 255);

    private final Rect AREA = new Rect(235, 220, 150, 110);

    private Mat green = new Mat();
    private Mat orange = new Mat();
    private Mat purple = new Mat();

    private SleeveColor sleeveColor;

    public enum SleeveColor {
        GREEN,
        ORANGE,
        PURPLE,
        UNKNOWN
    }

    @Override
    public Mat processFrame(Mat input) {
        Mat searchArea = input.submat(AREA);
        Imgproc.cvtColor(searchArea, searchArea, Imgproc.COLOR_RGB2HSV);
        Imgproc.GaussianBlur(searchArea, searchArea, new Size(5, 5), 0);

        Core.inRange(searchArea, LOWER_GREEN, UPPER_GREEN, green);
        Core.inRange(searchArea, LOWER_ORANGE, UPPER_ORANGE, orange);
        Core.inRange(searchArea, LOWER_PURPLE, UPPER_PURPLE, purple);

        processSleeve();

        // Decorative
        Scalar boxColor;
        switch (sleeveColor) {
            case GREEN: boxColor =  new Scalar(0, 255, 0);
                                    break;
            case ORANGE: boxColor = new Scalar(255, 130, 0);
                                    break;
            case PURPLE: boxColor = new Scalar(255, 0, 230);
                                    break;
            default:    boxColor =  new Scalar(255, 0, 0);
        }
        Imgproc.rectangle(input, AREA, boxColor, 5);

        return input;
    }

    @SuppressLint("NewApi")
    private void processSleeve() {
        double[] sleeve = new double[]{
            Core.mean(purple).val[0],
            Core.mean(orange).val[0],
            Core.mean(green).val[0]
        };

        int mostColor = 0;
        for (int i = 0; i < sleeve.length; i++) {
            mostColor = sleeve[i] > sleeve[mostColor] ? i : mostColor;
        }

        switch (mostColor) {
            case 0: sleeveColor = SleeveColor.PURPLE;
                    break;
            case 1: sleeveColor = SleeveColor.ORANGE;
                    break;
            case 2: sleeveColor = SleeveColor.GREEN;
                    break;
            default: sleeveColor = SleeveColor.UNKNOWN;
        }
    }

    public SleeveColor getSleeveColor() {
        return sleeveColor;
    }
}
