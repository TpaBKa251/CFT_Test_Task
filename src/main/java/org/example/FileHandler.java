package org.example;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с файлами (чтение и вызов метода записи {@link WriterHandler#writeLine(File, Object)}).
 * Использована аннотация {@link RequiredArgsConstructor} библиотеки <a href="https://projectlombok.org/">Lombok</a> для создания конструктора.
 * Имеет поля:
 * <p>{@link FileHandler#writerHandler}
 * <p>{@link FileHandler#statCalculator}
 * <p>Имеет методы:
 * <p>{@link FileHandler#readAndWrite(List, File, File, File)}
 * <p>{@link FileHandler#getReaders(List)}
 * <p>{@link FileHandler#readLine(List, File, File, File)}
 * <p>{@link FileHandler#writeLine(String, File, File, File)}
 * <p>{@link FileHandler#closeReaders(List)}
 */
@RequiredArgsConstructor
public class FileHandler {
    /**
     * Экземпляр класса {@link WriterHandler}
     */
    private final WriterHandler writerHandler;
    /**
     * Экземпляр класса {@link StatCalculator}
     */
    private final StatCalculator statCalculator;

    /**
     * Основной метод класса. Читает входные файлы и записывает данные в выходные.
     * Для чтения и записи использует методы {@link FileHandler#getReaders(List)} и {@link FileHandler#readLine(List, File, File, File)}
     * @param inputFiles список входных файлов в формате {@code String}
     * @param intFile выходной файл для целых чисел
     * @param floatFile выходной файл для вещественных чисел
     * @param strFile выходной файл для строк
     */
    public void readAndWrite(List<String> inputFiles, File intFile, File floatFile, File strFile) {
        // Лист ридеров для входных файлов
        List<BufferedReader> readers = getReaders(inputFiles);
        // Чтение и запись строки
        readLine(readers, intFile, floatFile, strFile);
        // Закрытие всех ридеров
        closeReaders(readers);
        // Закрытие всех райтеров
        writerHandler.closeAllWriters();
    }

    /**
     * Метод создания ридеров для входных файлов. Если ридер не удается создать, то выводит ошибку об этом и продолжает работу с другими файлами
     * @param inputFiles список входных файлов в формате {@code String}
     * @return список ридеров класса {@link  BufferedReader}
     * @see Style#setErrBold()
     * @see Style#setErr(String)
     */
    private List<BufferedReader> getReaders(List<String> inputFiles) {
        List<BufferedReader> readers = new ArrayList<>();
        for (String inputFile : inputFiles) {
            try {
                // Попытка создать ридер для входного файла
                readers.add(new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8)));
            } catch (FileNotFoundException e) {
                // Если входной файл не найден, то выводится ошибка об этом и программа продолжает работу
                System.err.println(
                        Style.setErrBold() +
                        Style.setErr("Файл " + inputFile + " не найден. " +
                        "Программа продолжит работу с остальными файлами (если они были указаны)\n"));
            }
        }
        return readers;
    }

    /**
     * Метод для чтения строк входных файлов и вызова метода записи {@link FileHandler#writeLine(String, File, File, File)}.
     * Если строку не удалось прочитать, программа продолжает работу с другими строками
     * @param readers список ридеров класса {@link  BufferedReader}
     * @param intFile выходной файл для целых чисел
     * @param floatFile выходной файл для вещественных чисел
     * @param strFile выходной файл для строк
     * @see Style#setErrBold()
     * @see Style#setErr(String)
     */
    private void readLine(List<BufferedReader> readers, File intFile, File floatFile, File strFile) {
        boolean filesNotEmpty = true;

        while (filesNotEmpty) {
            filesNotEmpty = false;

            for (BufferedReader reader : readers) {
                String line = "";

                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    System.out.println(
                            Style.setErrBold() +
                            Style.setErr("Не удалось прочитать строку. " + e.getMessage() + "\n"));
                }

                if (line != null && !line.isEmpty()) {
                    filesNotEmpty = true;
                    // Вызов метода записи
                    writeLine(line, intFile, floatFile, strFile);
                }
            }
        }
    }

    /**
     * Метод записи данных в выходные файлы.
     * <p>Вначале пытается преобразовать строку в {@code long}.
     * Если это получилось, тогда обновляет статистику целых чисел с помощью метода {@link StatCalculator#updateIntStat(long)}
     * и вызывает метод записи {@link WriterHandler#writeLine(File, Object)} для записи данных в файл целых чисел.
     * <p>Если не получилось преобразовать в {@code long}, тогда происходит попытка преобразования в {@code double}.
     * Если это получилось, тогда обновляет статистику вещественных чисел с помощью метода {@link StatCalculator#updateFloatStat(double)}
     * и вызывает метод записи {@link WriterHandler#writeLine(File, Object)} для записи данных в файл вещественных чисел.
     * <p>Если не получилось преобразовать ни в {@code long}, ни в {@code double},
     * тогда обновляет статистику строк с помощью метода {@link StatCalculator#updateStringStat(String)}
     * и вызывает метод записи {@link WriterHandler#writeLine(File, Object)} для записи данных в файл строк.
     * @param line строка во входном файле
     * @param intFile файл целых чисел
     * @param floatFile файл вещественных чисел
     * @param strFile файл строк
     */
    private void writeLine(String line, File intFile, File floatFile, File strFile) {
        // Попытка преобразования в long
        try {
            // Если не получилось преобразовать, то переходит в следующий блок try-catch
            long n = Long.parseLong(line);
            // Если получилось преобразовать, то обновляется статистика, и данные записываются в выходной файл
            statCalculator.updateIntStat(n);
            // Вызов метода записи в классе WriterHandler
            writerHandler.writeLine(intFile, n);

        } catch (NumberFormatException e) {
            // Попытка преобразования в double
            try {
                double f = Double.parseDouble(line);
                // Если получилось преобразовать, то обновляется статистика, и данные записываются в выходной файл
                statCalculator.updateFloatStat(f);
                // Вызов метода записи в классе WriterHandler
                writerHandler.writeLine(floatFile, f);

            } catch (NumberFormatException e2) {
                // Если не получилось преобразовать ни в long, ни в double, то строка записывается в файл для строк
                // Обновление статистики
                statCalculator.updateStringStat(line);
                // Вызов метода записи в классе WriterHandler
                writerHandler.writeLine(strFile, line);
            }
        }
    }

    /**
     * Метод закрытия всех ридеров
     * @param readers список ридеров класса {@link BufferedReader}
     * @see Style#setErrBold()
     * @see Style#setErr(String)
     */
    private void closeReaders(List<BufferedReader> readers) {
        for (BufferedReader reader : readers) {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println(
                        Style.setErrBold() +
                        Style.setErr("Ошибка при закрытии файла: " + e.getMessage() + "\n"));
            }
        }
    }
}
