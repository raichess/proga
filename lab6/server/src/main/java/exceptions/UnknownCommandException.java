package exceptions;

/**
 * Обеспечивает исключение, если такой команды не существует
 *
 * @author raichess
 * @since 1.0
 */
public class UnknownCommandException extends Exception {
    public UnknownCommandException(String command) {
        super("Unknown command: " + command + " use help for more info");
    }
}
