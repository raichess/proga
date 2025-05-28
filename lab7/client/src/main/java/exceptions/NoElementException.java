package exceptions;

/**
 * Исключение если не существует элемента коллекции с указанным ID
 *
 * @author raichess
 * @see commands.RemoveByIdCommand
 * @since 1.0
 */
public class NoElementException extends Exception {
    public NoElementException(Long id) {
        super("No element in collection with id: " + id);
    }
    public NoElementException(String message) {
        super(message);
    }
}
