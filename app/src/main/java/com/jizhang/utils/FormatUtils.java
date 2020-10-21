package com.jizhang.utils;

import java.text.DecimalFormat;

public class FormatUtils {

    private FormatUtils() {
    }

    public static float formatFloat(String pattern, float value) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return Float.parseFloat(decimalFormat.format(value));
    }
}
