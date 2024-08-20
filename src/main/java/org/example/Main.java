package org.example;

import org.fusesource.jansi.AnsiConsole;

/**
 * Вся программа поделена на классы:
 * <p>
 * {@link Analyzer} - основной класс
 * <p>
 * {@link Parameters} - класс для хранения и парсинга параметров через вложенный класс {@link org.example.Parameters.ParseParameters}
 * <p>
 * {@link StatCalculator} - класс для ведения статистики
 * <p>
 * {@link WriterHandler} - класс для записи данных
 * <p>
 * {@link FileHandler} - класс для работы с файлами
 * <p>
 * {@link Style} - класс для добавления цвета и стилей текста в консоли
 *
 * <p>Здесь использованы следующие библиотеки: <a href="https://projectlombok.org/">Lombok</a>,
 * <a href="https://commons.apache.org/proper/commons-cli/">Apache Commons Cli</a>,
 * <a href=https://mvnrepository.com/artifact/org.fusesource.jansi/jansi">Jansi</a>
 * <p>Сборщик - Gradle 8.8
 */
public class Main {
    public static void main(String[] args) {
        // Инициализация Jansi
        AnsiConsole.systemInstall();

        System.out.println(Style.setOut("Программа начала работу", true));

        try {
        // Объект с параметрами, которые были переданы в программу
        Parameters parameters = Parameters.ParseParameters.parse(args);

        // Объект StatCalculator для подсчета статистики
        StatCalculator statCalculator = new StatCalculator();

        // Объект WriterHandler, который будет управлять созданием и закрытием BufferedWriter
        WriterHandler writerHandler = new WriterHandler(parameters.append());

        // Объект FileHandler, который будет заниматься чтением и записью данных
        FileHandler fileHandler = new FileHandler(writerHandler, statCalculator);

        // Основной объект Analyzer, который будет управлять процессом анализа
        Analyzer analyzer = new Analyzer(parameters, fileHandler, statCalculator);

        // Выполнение анализ
        analyzer.analyze();

        // В случае любой непредвиденной ошибки (которая не обрабатывается в процессе выполнения),
        // программа останавливает выполнение и сообщает об этом пользователю
        } catch (Exception e) {
            System.err.println(
                    Style.setErrBold()
                    + Style.setErr("Что-то пошло не так: " + e.getMessage() + ". Дальнейшее выполнение программы невозможно\n"));
        }

        // Завершение работы Jansi
        AnsiConsole.systemUninstall();
    }
}