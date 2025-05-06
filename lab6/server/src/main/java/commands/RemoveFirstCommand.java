package commands;

import exceptions.NoElementException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.Receiver;
import system.Request;

/**
 * Данная команда удаляет первый элемент из коллекции
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class RemoveFirstCommand implements Command {
    @Override
    public String execute(Request request) throws Exception{
        try {
            return Receiver.removeFirst(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    @Override
    public String getName() {
        return "remove_first: ";
    }

    @Override
    public String getDescription() {
        return "remove first element ";
    }
}
