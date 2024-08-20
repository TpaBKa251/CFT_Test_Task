package org.example;

import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class WriterHandler {
    private final Map<File, BufferedWriter> writerMap = new HashMap<>();
    private final boolean append;
    private boolean isPathCorrect = true;

    public void writeLine(File file, Object value) {
        // Если ключ (файл) отсутствует, то добавляется новое значение с помощью метода
        BufferedWriter writer = writerMap.computeIfAbsent(file, this::getWriter);

        // Запись данных в выходные файлы
        if (writer != null) {
            try {
                writer.write(value + "\n");

            // Если не удалось записать данные, то программа продолжит работу
            } catch (IOException e) {
                System.err.println(Style.setErrBold() + Style.setErr(
                        "Не удалось записаь строку " + value + " в файл " + file
                        + ". Однако вы сможете увидеть актуальную статистику (если использовали опции -s или -f)\n"));
            }
        }
    }

    private BufferedWriter getWriter(File file) {
        if (!isPathCorrect) {
            return null;
        }

        try {
            return new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, append));
        } catch (IOException e) {
            Path parentPath = file.toPath().getParent();

            if (parentPath != null) {
                try {
                    createDirectory(parentPath);

                    return new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, append));
                } catch (IOException e1) {
                    creationError();
                }
            } else {
                creationError();
            }
        }
        return null;
    }

    private void createDirectory(Path path) throws IOException {
        try {
            System.out.println("Указанная директория не найдена. Попытка создания директории: " + path);
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Не удалось создать указанную директорию: " + path);
            throw e;
        }
    }

    private void creationError() {
        System.err.println(Style.setErrBold() + Style.setErr(
                "Системе не удается найти или создать указанный путь (директорию) выходных файлов." +
                        " Программа продолжит работу, но выходные файлы не будут созданы и записаны." +
                        " Однако вы сможете увидеть статистику (если использовали опции -s или -f)\n"));

        isPathCorrect = false;
    }

    public void closeAllWriters() {
        for (BufferedWriter writer : writerMap.values()) {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println(Style.setErrBold() + Style.setErr("Ошибка при закрытии процесса записи: " + e.getMessage() + "\n"));
            }
        }
    }
}

