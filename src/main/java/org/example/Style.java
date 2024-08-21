package org.example;

import org.fusesource.jansi.Ansi;

/**
 * Класс для установки цвета и стилей текста в командной строке.
 * Использует библиотеку <a href=https://mvnrepository.com/artifact/org.fusesource.jansi/jansi">Jansi</a> для установки стилей и цвета.
 * Имеет статические методы:
 * <p>{@link Style#setErrBold()} - для добавления жирной красной надписи "ОШИБКА"
 * <p>{@link Style#setErr(String)} - для добавления текста ошибки
 * <p>{@link Style#setHelp(String, boolean, boolean)} - для добавления текста помощи
 * <p>{@link Style#setExample(String, boolean, boolean)} - для добавления примера
 * <p>{@link Style#setOut(String, boolean)} - для добавления сообщения об успехе
 * <p>{@link Style#applyStyle(Ansi, boolean, boolean)} - для применения начертаний
 */
public class Style {

    /**
     *
     * @return жирная строка "ОШИБКА" красного цвета
     */
    public static String setErrBold(){
        return Ansi.ansi()
                .fg(Ansi.Color.RED)
                .a(Ansi.Attribute.INTENSITY_BOLD)
                .a("\nОШИБКА:\n")
                .reset()
                .toString();
    }

    /**
     *
     * @param err текст ошибки класса {@link String}
     * @return текст ошибки красного цвета
     */
    public static String setErr(String err){
        return Ansi.ansi()
                .fg(Ansi.Color.RED)
                .a(err)
                .reset()
                .toString();
    }

    /**
     *
     * @param help текст помощи или подсказки класса {@link String}
     * @param bold флаг для установления жирного стиля
     * @param italic флаг для установления курсива
     * @return строку помощи желтого цвета.
     * Если установлен флаг {@code bold} в значение {@code true}, то строка дополнительно становится жирной.
     * Если установлен флаг {@code italic} в значение {@code true}, то строка дополнительно становится курсивной
     */
    public static String setHelp(String help, boolean bold, boolean italic){
        return applyStyle(Ansi.ansi().fg(Ansi.Color.YELLOW), bold, italic)
                .a(help)
                .reset()
                .toString();
    }

    /**
     *
     * @param example текст примера класса {@link String}
     * @param bold флаг для установления жирного стиля
     * @param italic флаг для установления курсива
     * @return строку примера синего цвета.
     * Если установлен флаг {@code bold} в значение {@code true}, то строка дополнительно становится жирной.
     * Если установлен флаг {@code italic} в значение {@code true}, то строка дополнительно становится курсивной
     */
    public static String setExample(String example, boolean bold, boolean italic){
        return applyStyle(Ansi.ansi().fg(Ansi.Color.BLUE), bold, italic)
                .a(example)
                .reset()
                .toString();
    }

    /**
     *
     * @param out текст сообщения об успехе класса {@link String}
     * @param bold флаг для установления жирного стиля
     * @return строку сообщения об успехе зеленого цвета.
     * Если установлен флаг {@code bold} в значение {@code true}, то строка дополнительно становится жирной
     */
    public static String setOut(String out, boolean bold){
        return applyStyle(Ansi.ansi().fg(Ansi.Color.GREEN), bold, false)
                .a(out)
                .reset()
                .toString();
    }

    private static Ansi applyStyle(Ansi ansi, boolean bold, boolean italic) {
        if (bold) {
            ansi.a(Ansi.Attribute.INTENSITY_BOLD);
        }
        if (italic) {
            ansi.a(Ansi.Attribute.ITALIC);
        }
        return ansi;
    }

}
