package exceptions;

/**
 * Обеспечивает исключение, если возникла ошибка в аргументе
 *
 * @author raichess
 * @since 1.0
 */
public class WrongArgumentException extends Exception {
    public WrongArgumentException(String argument) {
        super("Wrong argument in command: " + argument);
    }
}
