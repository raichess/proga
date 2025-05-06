package commands;

import managers.Receiver;
import system.Request;

/**
 * Данная команда добавляет новый элемент в коллекцию
 *
 *  @see Command
 *  @author raichess
 *  @since 1.0
 */
public class AddCommand implements Command {

    @Override
    public String execute(Request request) throws Exception{
        try {
            return Receiver.addNewEl(request);
        }catch (Exception e) {
            return e.getMessage();
        }
    }
    @Override
    public String getName() {
        return "add {element}: ";
    }

    @Override
    public String getDescription() {
        return "add new vehicle in collection";
    }
}
