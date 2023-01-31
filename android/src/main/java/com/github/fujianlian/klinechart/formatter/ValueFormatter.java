package com.github.fujianlian.klinechart.formatter;

import android.os.Build;
import com.github.fujianlian.klinechart.base.IValueFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Value格式化类
 * Created by tifezh on 2016/6/21.
 */

public class ValueFormatter implements IValueFormatter {

    public static Integer priceRightLength = 4;

    public static Integer volumeRightLength = 4;

    @Override
    public String format(float value) {
        return this._format(value, true, true);
    }

    public String formatVolume(float value) {
        return this._format(value, false, true);
    }

    public static String format(float value, int rightLength, boolean fillzero) {
    	// NumberFormat format = NumberFormat.getInstance();
     //    format.setGroupingUsed(false);
     //    format.setRoundingMode(RoundingMode.DOWN);
     //    format.setMaximumFractionDigits(rightLength);
     //    if (fillzero) {
     //    	format.setMinimumFractionDigits(rightLength);
     //    }
     //    return format.format(value);


        String numberString = String.valueOf(value);
        numberString = new BigDecimal(numberString).toPlainString();
        int dotIndex = numberString.indexOf(".");
        if (dotIndex == -1) {
            numberString = numberString + ".";
            dotIndex = numberString.length() - 1;
        }
        int reloadLength = dotIndex + 1 + rightLength;
        if (numberString.length() < reloadLength) {
            numberString += String.format("%0" + (reloadLength - numberString.length()) + "d", 0);
        }
        numberString = numberString.substring(0, rightLength > 0 ? reloadLength : reloadLength - 1);
        return numberString;
    }

    public String _format(float value, boolean isPrice, boolean fillzero) {
        Integer rightLength = isPrice ? this.priceRightLength : this.volumeRightLength;
        return format(value, rightLength, fillzero);
    }

}
