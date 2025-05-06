package commands;

import exceptions.WrongArgumentException;
import managers.CommandManager;
import managers.Receiver;
import system.Request;

/**
 * Данная команда выводит историю введенных команд
 * Максимум 6 последних команд
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class HistoryCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.getHistory(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "history: ";
    }

    @Override
    public String getDescription() {
        return "show last six commands";
    }
}
