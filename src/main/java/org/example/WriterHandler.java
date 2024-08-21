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

/**
 * Класс для записи данных в выходные файлы.
 * Использована аннотация {@link RequiredArgsConstructor} библиотеки <a href="https://projectlombok.org/">Lombok</a> для создания конструктора.
 * Содержит поля:
 * <p>{@link WriterHandler#writerMap} для хранения файлов и райтеров в формате ключ-значение,
 * <p>{@link WriterHandler#append} для установки режима добавления в существующие файлы,
 * <p>{@link WriterHandler#isPathCorrect} для определения корректности заданного пути для выходных файлов.
 * <p>Основной метод класса {@link WriterHandler#writeLine(File, Object)} использует метод {@link WriterHandler#getWriter(File)} для создания райтера для файла, если он отсутствует,
 * и записывает данные в соответствующие выходные файлы.
 * Метод {@link WriterHandler#getWriter(File)} использует вспомогательный метод {@link WriterHandler#createDirectory(Path)} для создания директории для выходных файлов, если она отсутствует.
 * Метод {@link WriterHandler#creationError()} выводит ошибку, если не удалось создать директорию для выходных файлов
 * Метод {@link WriterHandler#closeAllWriters()} используется для закрытия всех райтеров.
 */
@RequiredArgsConstructor
public class WriterHandler {
    /**
     * Поле которое содержит файлы и райтеры в формате ключ(файл)-значение(райтер)
     */
    private final Map<File, BufferedWriter> writerMap = new HashMap<>();
    /**
     * Флаг добавления данных в существующие файлы
     */
    private final boolean append;
    /**
     * Флаг корректности пути. Если не удалось создать директорию или райтер (после создания директории) для выходных файлов,
     * то флаг становится {@code false}, поле чего райтеры и директории больше не создаются
     */
    private boolean isPathCorrect = true;

    /**
     * Основной метод класса. Получает или создает райтер для выходного файла,
     * после чего записыват в файл данные.
     * Для создания райтера использует метод {@link WriterHandler#getWriter(File)}
     * @param file выходной файл
     * @param value данные для записи
     * @see Style#setErrBold()
     * @see Style#setErr(String)
     */
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
                        "Не удалось записать строку " + value + " в файл " + file
                        + ". Однако вы сможете увидеть актуальную статистику (если использовали опции -s или -f)\n"));
            }
        }
    }

    /**
     * Вспомогательный метод для {@link WriterHandler#writeLine(File, Object)}.
     * Пытается создать райтер, если это не получается, то создает директорию с помощью метода {@link WriterHandler#createDirectory(Path)}.
     * Если директорию или райтер (после создания директории) не получается создать,
     * то выводит ошибку пользователю с помощью метода {@link WriterHandler#creationError()}
     * и устанавливает флагу {@link WriterHandler#isPathCorrect} значение {@code false}
     * @param file выходной файл
     * @return экземпляр класса {@link BufferedWriter} для выходного файла
     */
    private BufferedWriter getWriter(File file) {
        if (!isPathCorrect) {
            return null;
        }

        try {
            // Попытка создать райтер для файла
            return new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, append));
        } catch (IOException e) {
            // Если выдало исключение, то создается директория для файла,
            // после чего происходит вторая попытка создания райтера
            Path parentPath = file.toPath().getParent();

            if (parentPath != null) {
                try {
                    createDirectory(parentPath);

                    return new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, append));
                } catch (IOException e1) {
                    // Если снова выдало исключение, то выводится ошибка, о невозможности создания выходных файлов
                    creationError();
                }
            } else {
                creationError();
            }
        }
        return null;
    }

    /**
     * Вспомогательный метод для создания директории для выходных файлов, если она отсутствует
     * @param path путь директории
     * @throws IOException если не удается создать директорию
     */
    private void createDirectory(Path path) throws IOException {
        try {
            System.out.println("Указанная директория не найдена. Попытка создания директории: " + path);
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Не удалось создать указанную директорию: " + path);
            throw e;
        }
    }

    /**
     * Вспомогательный метод для вывода ошибки о невозможности создания выходных файлов
     * @see Style#setErrBold()
     * @see Style#setErr(String)
     */
    private void creationError() {
        System.err.println(Style.setErrBold() + Style.setErr(
                "Системе не удается найти или создать указанный путь (директорию) выходных файлов." +
                        " Программа продолжит работу, но выходные файлы не будут созданы и записаны." +
                        " Однако вы сможете увидеть статистику (если использовали опции -s или -f)\n"));

        isPathCorrect = false;
    }

    /**
     * Метод для закрытия всех райтеров
     * @see Style#setErrBold()
     * @see Style#setErr(String)
     */
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

