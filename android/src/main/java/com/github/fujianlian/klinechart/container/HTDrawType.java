package com.github.fujianlian.klinechart.container;

public enum HTDrawType {

    none,

    line,

    horizontalLine,

    verticalLine,

    halfLine,

    parallelLine,

    rectangle,

    parallelogram;

    public static HTDrawType drawTypeFromRawValue(int value) {
        switch (value) {
            case 1: {
                return line;
            }
            case 2: {
                return horizontalLine;
            }
            case 3: {
                return verticalLine;
            }
            case 4: {
                return halfLine;
            }
            case 5: {
                return parallelLine;
            }
            case 101: {
                return rectangle;
            }
            case 102: {
                return parallelogram;
            }
            default: {
                return none;
            }
        }
    }

    public int count() {
        if (this == line || this == horizontalLine || this == verticalLine || this == halfLine || this == rectangle) {
            return 2;
        }
        if (this == parallelLine || this == parallelogram) {
            return 3;
        }
        return 1;
    }

}
