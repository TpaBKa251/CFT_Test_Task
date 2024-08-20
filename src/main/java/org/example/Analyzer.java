package org.example;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RequiredArgsConstructor
public class Analyzer {
    private final Parameters parameters;
    private final FileHandler fileHandler;
    private final StatCalculator statCalculator;

    public void analyze() {
        String outputPath = getPath();

        // Выходные файлы
        File intFile = new File(outputPath + "integers.txt");
        File floatFile = new File(outputPath + "floats.txt");
        File strFile = new File(outputPath + "strings.txt");

        fileHandler.readAndWrite(parameters.inputFiles(), intFile, floatFile, strFile);
        // Вывод созданных выходных файлов
        System.out.println(Style.setOut("\nРезультат:\n", true)
                + "    Выходные файлы: " + getOutputFiles(intFile, floatFile, strFile));
        // Вывод статистики
        statCalculator.printStat(parameters);
        System.out.println(Style.setOut("\nПрограмма успешно завершила работу", true));
    }

    private String getPath() {
        Path currentPath = Paths.get("");
        return Objects.requireNonNullElse(parameters.path(), currentPath.toAbsolutePath().toString()) + File.separator
                + Objects.requireNonNullElse(parameters.prefix(), "");
    }

    private String getOutputFiles(File fileInt, File fileFloat, File fileStr) {
        String outputFiles = "";

        if (fileInt.exists()){
            outputFiles += fileInt + ", ";
        }
        if (fileFloat.exists()){
            outputFiles += fileFloat + ", ";
        }
        if (fileStr.exists()){
            outputFiles += fileStr.toString();
        }

        return outputFiles;
    }
}
