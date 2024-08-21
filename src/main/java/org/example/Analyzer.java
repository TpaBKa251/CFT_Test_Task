package org.example;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Основной класс программы для анализа входных данных.
 * Использована аннотация {@link RequiredArgsConstructor} библиотеки <a href="https://projectlombok.org/">Lombok</a> для создания конструктора.
 * Имеет поля:
 * <p>{@link Analyzer#parameters}
 * <p>{@link Analyzer#fileHandler}
 * <p>{@link Analyzer#statCalculator}
 * <p>Имеет методы:
 * <p>{@link Analyzer#analyze()}
 * <p>{@link Analyzer#getPath()}
 * <p>{@link Analyzer#getOutputFiles(File, File, File)}
 */
@RequiredArgsConstructor
public class Analyzer {
    /**
     * Экземпляр класса {@link Parameters}
     */
    private final Parameters parameters;
    /**
     * Экземпляр класса {@link FileHandler}
     */
    private final FileHandler fileHandler;
    /**
     * Экземпляр класса {@link StatCalculator}
     */
    private final StatCalculator statCalculator;

    /**
     * Основной метод класса. Получает пуь до выходных файлов с помощью метода {@link Analyzer#getPath()}.
     * После чего вызывает метод {@link FileHandler#readAndWrite(List, File, File, File)} для чтения и записи.
     * Затем выводит выходные файлы с помощью метода {@link Analyzer#getOutputFiles(File, File, File)},
     * выводит статистику с помощью метода {@link StatCalculator#printStat(Parameters)}.
     * В конце выводит сообщение об успешном завершении программы.
     * @see Style#setOut(String, boolean)
     */
    public void analyze() {
        String outputPath = getPath();

        // Выходные файлы
        File intFile = new File(outputPath + "integers.txt");
        File floatFile = new File(outputPath + "floats.txt");
        File strFile = new File(outputPath + "strings.txt");

        fileHandler.readAndWrite(parameters.inputFiles(), intFile, floatFile, strFile);
        // Вывод созданных выходных файлов
        System.out.println(Style.setOut("\nРезультат:\n", true)
                + "    Выходные файлы (созданные или измененные):\n" + getOutputFiles(intFile, floatFile, strFile));
        // Вывод статистики
        statCalculator.printStat(parameters);
        System.out.println(Style.setOut("\nПрограмма успешно завершила работу", true));
    }

    /**
     * Метод для получения пути до выходных файлов.
     * Использует данные из {@link Parameters#path()} и {@link Parameters#prefix()}.
     * Если эти параметры не заданы, то используются значения по умолчанию:
     * путь до текущей папки и пустой префикс
     * @return путь до выходных файлов в формате строки класса {@link String}
     */
    private String getPath() {
        Path currentPath = Paths.get("");
        return Objects.requireNonNullElse(parameters.path(), currentPath.toAbsolutePath().toString()) + File.separator
                + Objects.requireNonNullElse(parameters.prefix(), "");
    }

    /**
     * Метод для вывода выходных файлов. Если количество добавленных в соответствующий файл элементов не равно нулю,
     * тогда этот файл добавляется в строку
     * @param fileInt файл целых чисел
     * @param fileFloat файл вещественных чисел
     * @param fileStr файл строк
     * @return строку с выходными файлами в формате пути класса {@link String}
     */
    private String getOutputFiles(File fileInt, File fileFloat, File fileStr) {
        String outputFiles = "";

        if (statCalculator.getCntInt() != 0){
            outputFiles += "        " + fileInt + "\n";
        }
        if (statCalculator.getCntFloat() != 0){
            outputFiles += "        " + fileFloat + "\n";
        }
        if (statCalculator.getCntStr() != 0){
            outputFiles += "        " + fileStr;
        }

        return outputFiles;
    }
}
