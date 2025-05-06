package exceptions;

/**
 * Обеспечивает исключение при появлении рекурсии во время выполнения скрипт файла
 * @see commands.ExecuteScriptCommand
 * @author raichess
 * @since 1.0
 */
public class RecursionException extends Exception {
    public RecursionException() {
        super("Recursion in file");
    }
}
