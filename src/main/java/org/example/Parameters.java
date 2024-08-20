package org.example;

import lombok.Builder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.List;

/**
 * Класс с данными об аргументах в командной строке (параметрами).
 * Содержит вложенный класс {@link ParseParameters} с методом {@link ParseParameters#parse(String[])} который читает аргументы командной строки,
 * обновляет значение параметров и возвращает с помощью билдера готовый экземпляр класса {@link Parameters}
 *
 * <p>Использована аннотация {@link Builder} библиотеки <a href="https://projectlombok.org/">Lombok</a> для реализации паттерна строителя
 * <p>Использована библиотека <a href="https://commons.apache.org/proper/commons-cli/">Apache Commons Cli</a> для анализа аргументов командной строки
 * @param inputFiles список входных файлов в виде строк
 * @param path путь до папки, где находятся выходные файлы, заданный пользователем
 * @param prefix префикс для выходных файлов, заданный пользователем
 * @param append флаг о добавлении данных в существующие файлы. Обозначает нужно ли добавлять новые данные в существующие файлы или перезаписывать файлы с новыми данными
 * @param shortStat флаг краткой статистики. Обозначает нужно ли выводить краткую статистику
 * @param fullStat флаг полной статистики. Обозначает нужно ли выводить полную статистику
  */
@Builder
public record Parameters(
        List<String> inputFiles, // список входящих файлов
        String path, // путь до выходящих файлов, заданный пользователем
        String prefix, // префикс выходящих файлов, заданный пользователем
        Boolean append, // флаг о добавлении данных или перезаписывании файлов
        Boolean shortStat, // флаг краткой статистики
        Boolean fullStat // флаг полной статистики
) {
    /**
     * Вложенный класс, занимающийся парсингом аргументов с помощью библиотеки Apache Commons Cli.
     * Имеет метод {@link ParseParameters#parse(String[])}, которому подаются аргументы командной строки.
      */
    static class ParseParameters {
        /**
         * Метод парсинга аргументов командной строки
         * Вначале с помощью классов {@link Options} и {@link org.apache.commons.cli.Option} создаются все опции.
         * Затем начинается парсинг аргументов командной строки.
         * Если парсинг прошел успешно (не выдало исключение {@link ParseException}), то через класс {@code ParametersBuilder},
         * сгенерированный аннотацией Lombok {@link Builder}, строится экземпляр класса {@link Parameters}, который возвращает данный метод.
         * Если вылетело исключение, то программа останавливает работу,
         * а пользователю пишут причину и подсказку с примером, какие опции поддерживает программа и как ее правильно запускать
         * @param args аргументы командной строки
         * @return экземпляр класса {@link Parameters}
         */
        public static Parameters parse(String[] args) {
            // Создание объекта Options и добавление всех возможных опций
            Options options = new Options();

            options.addOption("s", "shortStat", false, "Prints short statistics");
            options.addOption("f", "fullStat", false, "Prints full statistics");
            options.addOption("a", "add", false, "Appends data into existing files");
            options.addOption("p", "prefix", true, "Prefix for output files");
            options.addOption("o", "output", true, "Path to output files");

            CommandLineParser parser = new DefaultParser();
            ParametersBuilder builder = Parameters.builder();

            // Парсинг аргументов командной строки
            try {
                CommandLine cmd = parser.parse(options, args);

                // Построение параметров на основе аргументов командной строки
                builder = Parameters.builder()
                        .inputFiles(List.of(cmd.getArgs()))
                        .path(cmd.getOptionValue("o"))
                        .prefix(cmd.getOptionValue("p"))
                        .append(cmd.hasOption("a"))
                        .shortStat(cmd.hasOption("s"))
                        .fullStat(cmd.hasOption("f"));

            // Если в процессе парсинга вылетает исключение, то программа останавливает работу,
            // пишет пользователю причину и пояснение как нужно запускать программу, какие опции она поддерживает
            } catch (ParseException e) {
                // Вывод ошибки
                System.err.println(
                        Style.setErrBold()
                        + Style.setErr("Ошибка обработки аргументов: " + e.getMessage() + ". Дальнейшее выполнение программы невозможно\n")
                        + Style.setHelp("\nСтрока запуска программы и возможные опции:", true, false) + Ansi.ansi().fg(Ansi.Color.YELLOW));

                // Вывод пояснения (подсказки)
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar build/libs/CFT_Test_Task-1.0-SNAPSHOT.jar [options] [inputFiles]", options);

                // Вывод примера запуска
                System.out.println(
                        Ansi.ansi().reset()
                        + Style.setExample("\n\nПример запуска программы из корневой папки проекта:", true, false)
                        + Style.setExample("\n   java -jar build/libs/CFT_Test_Task-1.0-SNAPSHOT.jar -o path\\to\\files" +
                                " -s -f -a -p your-prefix inputFile1.txt inputFile2.txt", false, false));

                // Завершение работы Jansi, так как в Main эта строчка не выполнится
                AnsiConsole.systemUninstall();

                // Принудительная остановка программы, иначе вылетит второе исключение в Main
                System.exit(1);
            }

            return builder.build();
        }
    }
}
