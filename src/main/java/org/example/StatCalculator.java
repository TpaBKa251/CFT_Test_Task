package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StatCalculator {
    private long cntInt = 0;
    private long cntStr = 0;
    private long cntFloat = 0;

    private long maxInt = Long.MIN_VALUE;
    private long minInt = Long.MAX_VALUE;
    private long sumInt = 0;

    private double maxFloat = -Double.MAX_VALUE;
    private double minFloat = Double.MAX_VALUE;
    private BigDecimal sumFloat = BigDecimal.ZERO;

    private long maxStr = Long.MIN_VALUE;
    private long minStr = Long.MAX_VALUE;

    public void updateIntStat(long n) {
        cntInt++;
        maxInt = Math.max(maxInt, n);
        minInt = Math.min(minInt, n);
        sumInt += n;
    }

    public void updateFloatStat(double f) {
        cntFloat++;
        maxFloat = Math.max(maxFloat, f);
        minFloat = Math.min(minFloat, f);
        sumFloat = sumFloat.add(BigDecimal.valueOf(f));
    }

    public void updateStringStat(String str) {
        cntStr++;
        maxStr = Math.max(maxStr, str.length());
        minStr = Math.min(minStr, str.length());
    }

    public void printStat(Parameters parameters) {
        if (parameters.shortStat()) {
            System.out.println(
                    "\nКраткая статистика:\n" +
                            "   integers: " + cntInt + "\n" +
                            "   floats: " + cntFloat + "\n" +
                            "   strings: " + cntStr + "\n");
        }

        if (parameters.fullStat()) {
            BigDecimal sumIntDecimal = BigDecimal.valueOf(sumInt);
            BigDecimal cntIntBigDecimal = BigDecimal.valueOf(cntInt);

            double middleInt = (cntInt == 0) ? 0 : sumIntDecimal.divide(cntIntBigDecimal, 16, RoundingMode.HALF_UP).doubleValue();
            long middleInt2 = (cntInt == 0) ? 0 : sumInt / cntInt;
            double middleFloat = (cntFloat == 0) ? 0.0 : sumFloat.divide(BigDecimal.valueOf(cntFloat), 16, RoundingMode.HALF_UP).doubleValue();

            System.out.println(
                    "\nПолная статистика:\n" +
                            "    integers:\n" +
                            "       Кол-во: " + cntInt + "\n" +
                            "       Макс: " + maxInt + "\n" +
                            "       Мин: " + minInt + "\n" +
                            "       Сум: " + sumInt + "\n" +
                            "       Сред: " + middleInt + " (целое = " + middleInt2 + ")\n" +
                            "    floats:\n" +
                            "       Кол-во: " + cntFloat + "\n" +
                            "       Макс: " + maxFloat + "\n" +
                            "       Мин: " + minFloat + "\n" +
                            "       Сум: " + sumFloat + "\n" +
                            "       Сред: " + middleFloat + "\n" +
                            "    strings:\n" +
                            "       Кол-во: " + cntStr + "\n" +
                            "       Макс: " + maxStr + "\n" +
                            "       Мин: " + minStr);
        }
    }
}
