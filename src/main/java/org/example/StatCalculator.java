package org.example;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс для ведения и вывода статистки. Имеет методы:
 * <p>{@link StatCalculator#updateIntStat(long)} для обновления статистики целых чисел
 * <p>{@link StatCalculator#updateFloatStat(double)} для обновления статистики вещественных чисел
 * <p>{@link StatCalculator#updateStringStat(String)} для обновления статистики трок
 * <p>{@link StatCalculator#printStat(Parameters)} для вывода статистики
 */
public class StatCalculator {
    /**
     * Поля количества элементов каждого типа
     * <p>Использована аннотация {@link Getter} библиотеки <a href="https://projectlombok.org/">Lombok</a>
     * для создания методов get() для этих 3-х полей.
     */
    @Getter
    private long cntInt = 0;
    @Getter
    private long cntStr = 0;
    @Getter
    private long cntFloat = 0;

    /**
     * Поля полной статистики для целых чисел
     */
    private long maxInt = Long.MIN_VALUE;
    private long minInt = Long.MAX_VALUE;
    private long sumInt = 0;

    /**
     * Поля полной статистики для вещественных чисел.
     * Для суммы используется класс {@link BigDecimal}
     */
    private double maxFloat = -Double.MAX_VALUE;
    private double minFloat = Double.MAX_VALUE;
    private BigDecimal sumFloat = BigDecimal.ZERO;

    /**
     * Поля полной статистики для строк
     */
    private long maxStr = Long.MIN_VALUE;
    private long minStr = Long.MAX_VALUE;

    /**
     * Обновляет статистику целых чисел (количество, максимальное и минимальное значение, сумму)
     * @param n целое число
     */
    public void updateIntStat(long n) {
        cntInt++;
        maxInt = Math.max(maxInt, n);
        minInt = Math.min(minInt, n);
        sumInt += n;
    }

    /**
     * Обновляет статистику вещественных чисел (количество, максимальное и минимальное значение, сумму).
     * Для вычисления суммы используется класс {@link BigDecimal} для большей точности
     * @param f вещественное число
     */
    public void updateFloatStat(double f) {
        cntFloat++;
        maxFloat = Math.max(maxFloat, f);
        minFloat = Math.min(minFloat, f);
        sumFloat = sumFloat.add(BigDecimal.valueOf(f));
    }

    /**
     * Обновляет статистику строк (количество, максимальная и минимальная длина)
     * @param str строка
     */
    public void updateStringStat(String str) {
        cntStr++;
        maxStr = Math.max(maxStr, str.length());
        minStr = Math.min(minStr, str.length());
    }

    /**
     * Метод вывода краткой либо полной статистики (либо обоих сразу)
     * @param parameters параметры командной строки класса {@link Parameters}
     */
    public void printStat(Parameters parameters) {
        // Вывод краткой статистики
        if (parameters.shortStat()) {
            System.out.println(
                    "\nКраткая статистика:\n" +
                            "   integers: " + cntInt + "\n" +
                            "   floats: " + cntFloat + "\n" +
                            "   strings: " + cntStr + "\n");
        }

        // Вывод полной статистики
        if (parameters.fullStat()) {
            BigDecimal sumIntDecimal = BigDecimal.valueOf(sumInt);
            BigDecimal cntIntBigDecimal = BigDecimal.valueOf(cntInt);

            // Вычисление средних значений
            // Для вещественных чисел используется класс BigDecimal для большей точности вычисления
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
