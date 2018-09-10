package com.gdlife.candypie.utils;

import java.text.DecimalFormat;

public class NumberUtils {

    public static String qianweifenge(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.0");
        String ss = df.format(num);
        return ss;
    }

    public static String qianweifengecoin(double num) {
        DecimalFormat df = new DecimalFormat("#,##0");
        String ss = df.format(num);
        return ss;
    }
}
