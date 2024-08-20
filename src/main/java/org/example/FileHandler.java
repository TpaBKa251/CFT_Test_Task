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

@RequiredArgsConstructor
public class FileHandler {
    private final WriterHandler writerHandler;
    private final StatCalculator statCalculator;

    public void readAndWrite(List<String> inputFiles, File intFile, File floatFile, File strFile) {
        List<BufferedReader> readers = getReaders(inputFiles);
        readLine(readers, intFile, floatFile, strFile);
        closeReaders(readers);
        writerHandler.closeAllWriters();
    }

    private List<BufferedReader> getReaders(List<String> inputFiles) {
        List<BufferedReader> readers = new ArrayList<>();
        for (String inputFile : inputFiles) {
            try {
                readers.add(new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8)));
            } catch (FileNotFoundException e) {
                System.err.println(
                        Style.setErrBold() +
                        Style.setErr("Файл " + inputFile + " не найден. " +
                        "Программа продолжит работу с остальными файлами (если они были указаны)\n"));
            }
        }
        return readers;
    }

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
                    writeLine(line, intFile, floatFile, strFile);
                }
            }
        }
    }

    private void writeLine(String line, File intFile, File floatFile, File strFile) {
        try {
            long n = Long.parseLong(line);
            statCalculator.updateIntStat(n);
            writerHandler.writeLine(intFile, n);

        } catch (NumberFormatException e) {
            try {
                double f = Double.parseDouble(line);
                statCalculator.updateFloatStat(f);
                writerHandler.writeLine(floatFile, f);

            } catch (NumberFormatException e2) {
                statCalculator.updateStringStat(line);
                writerHandler.writeLine(strFile, line);
            }
        }
    }

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
