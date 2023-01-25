package org.firstinspires.ftc.teamcode.util.color;

public enum TapeMeasureColor {
    PURPLE (new RGB(500, 500, 500), 0),
    WHITE(new RGB(234, 434, 475), 1),
    BLUE(new RGB(93, 202,333), 2),
    GREEN(new RGB(96, 221, 240), 3),
    ORANGE(new RGB(263, 263, 158), 4),
    RED(new RGB(0, 0, 0), 5),
    YELLOW(new RGB(0, 0, 0), 6),
    UNKNOWN(new RGB(-10, -10, -10), 10);

    private final RGB rgbColor;
    private final int order;

    TapeMeasureColor(RGB rgbColor, int order) {
        this.rgbColor = rgbColor;
        this.order = order;
    }

    public RGB getColor() {
        return rgbColor;
    }

    public static TapeMeasureColor[] getAllColors() {
        return new TapeMeasureColor[]{
                PURPLE,
                WHITE,
                BLUE,
                GREEN,
                ORANGE,
                RED,
                YELLOW
        };
    }

    public int getOrder() {
        return order;
    }
}
