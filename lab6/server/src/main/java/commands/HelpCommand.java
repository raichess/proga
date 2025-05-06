package commands;

import exceptions.WrongArgumentException;
import managers.CommandManager;
import managers.Receiver;
import system.Request;

import java.util.LinkedHashMap;

/**
 * Данная команда выводит все возможные команды и их описание
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class HelpCommand implements Command {

    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.getHelp(request);
        } catch (Exception e) {
            e.printStackTrace(); // для отладки — вывод в консоль
            return e.getMessage() != null ? e.getMessage() : "Unknown error occurred.";
        }
    }

    @Override
    public String getName() {
        return "help: ";
    }

    @Override
    public String getDescription() {
        return "command to get information";
    }
}
