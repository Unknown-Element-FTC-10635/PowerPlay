package org.firstinspires.ftc.teamcode.visionpipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ConeStackDetection extends OpenCvPipeline {
    private final Scalar LOWER_BLUE = new Scalar(100, 70, 90);
    private final Scalar UPPER_BLUE = new Scalar(130, 255, 255);

    private final Rect BUFFERED_CENTER = new Rect(230, 0, 80, 480);

    private Mat proc = new Mat();
    private Mat blue = new Mat();

    private StackDirections direction = StackDirections.UNKNOWN;
    private StackEstimate estimate = StackEstimate.EMPTY;

    public enum StackDirections {
        LEFT,
        RIGHT,
        CENTER,
        UNKNOWN
    }

    public enum StackEstimate {
        EMPTY,
        NORMAL,
        FILLED
    }

    @Override
    public Mat processFrame(Mat mat) {
        Imgproc.cvtColor(mat, proc, Imgproc.COLOR_RGB2HSV);
        Imgproc.GaussianBlur(proc, proc, new Size(5, 5), 0);

        Core.inRange(proc, LOWER_BLUE, UPPER_BLUE, blue);

        // 5% of a 640x480 image
        if (Core.countNonZero(blue) > 15360) {
            // 15% of a 640x480 image
            if (Core.countNonZero(blue) > 46080) {
                estimate = StackEstimate.FILLED;
            } else {
                estimate = StackEstimate.NORMAL;
            }

            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(blue, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            contours.sort(new Comparator<MatOfPoint>() {
                @Override
                public int compare(MatOfPoint o1, MatOfPoint o2) {
                    int area1 = (int) Imgproc.contourArea(o1);
                    int area2 = (int) Imgproc.contourArea(o2);
                    return (area2 - area1);
                }
            });
            if (!contours.isEmpty()) {
                Rect coneRect = Imgproc.boundingRect(contours.get(0));
                int centerX = coneRect.x + (coneRect.width / 2);

                if (BUFFERED_CENTER.contains(new Point(centerX, 240))) {
                    direction = StackDirections.CENTER;
                } else if (centerX < BUFFERED_CENTER.x) {
                    direction = StackDirections.RIGHT;
                } else if (centerX > BUFFERED_CENTER.x + BUFFERED_CENTER.width) {
                    direction = StackDirections.LEFT;
                }

                Imgproc.line(mat, new Point(centerX, 0), new Point(centerX, 480), new Scalar(0, 0, 0), 3);
                Imgproc.rectangle(mat, coneRect, new Scalar(0, 0, 255), 3);
            }
        } else {
            direction = StackDirections.UNKNOWN;
            estimate = StackEstimate.EMPTY;
        }

        Scalar boxColor = direction == StackDirections.CENTER ? new Scalar(0, 255, 0) : new Scalar(255, 0, 0);
        Imgproc.rectangle(mat, BUFFERED_CENTER, boxColor, 3);
        return mat;
    }

    public StackDirections getDirection() {
        return direction;
    }

    public StackEstimate getFrameEstimate() {
        return estimate;
    }
}
