package commands;

import managers.Receiver;
import system.Request;

/**
 * Данная команда выводит все элементы коллекции
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ShowCommand implements Command {

    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.showData(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "show: ";
    }

    @Override
    public String getDescription() {
        return "show data";
    }
}
