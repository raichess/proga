package commands;

import managers.Receiver;
import system.Request;

/**
 * Данная команда сортирует элементы по убыванию
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class DescendingCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.sortDescending(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "print_descending: ";
    }

    @Override
    public String getDescription() {
        return "print the elements of the collection in descending order (sorted by id) and save the new order";
    }
}
