package commands;

import managers.Receiver;
import system.Request;

/**
 * Данная команда сортирует коллекцию в обратном порядке
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ReorderCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.reorder(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "reorder: ";
    }

    @Override
    public String getDescription() {
        return "reorder the collection in reverse order.";
    }
}
