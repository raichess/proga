package commands;

import managers.Receiver;
import system.Request;

/**
 * Данная команда удаляет элемент из коллекции по его ID
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class RemoveByIdCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.removeById(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "remove_by_id id: ";
    }

    @Override
    public String getDescription() {
        return "remove element by id";
    }
}
