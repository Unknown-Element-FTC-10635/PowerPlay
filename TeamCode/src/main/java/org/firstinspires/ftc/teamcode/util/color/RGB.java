package org.firstinspires.ftc.teamcode.util.color;

import androidx.annotation.Nullable;

import java.util.Arrays;

public class RGB {

    private int[] color;

    public RGB(int r, int g, int b) {
        color = new int[]{r, g, b};
    }

    public int[] getColor() {
        return color;
    }

    public int getRed() {
        return color[0];
    }

    public int getGreen() {
        return color[1];
    }

    public int getBlue() {
        return color[2];
    }

    public boolean equals(RGB compare) {
        if (color[0] != compare.getRed()) {
            return false;
        }
        if (color[1] != compare.getGreen()) {
            return false;
        }
        if (color[2] != compare.getBlue()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "RGB{" +
                "Red=" + color[0] +
                ", Green=" + color[1] +
                ", Blue=" + color[2] +
                '}';
    }
}
