package commands;

import managers.Receiver;
import system.Request;


/**
 * Данная команда сохраняет коллекцию в XML файл
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class SaveCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.saveData(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "save: ";
    }

    @Override
    public String getDescription() {
        return "save collection to xml file";
    }
}
