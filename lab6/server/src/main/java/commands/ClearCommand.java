package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.Receiver;
import system.Request;

/**
 * Данная команда очищает коллекцию
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ClearCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.clearCollection(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "clear: ";
    }

    @Override
    public String getDescription() {
        return "clears the collection";
    }
}
