package com.yuhao.deepoj.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComputeUtils {
    public static int getRateScore(int numerator, int denominator) {
        BigDecimal result = new BigDecimal(numerator).divide(new BigDecimal(denominator), 2, RoundingMode.HALF_UP);
        return result.multiply(new BigDecimal(100)).intValue();
    }
}
