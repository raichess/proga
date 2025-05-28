package commands;

import managers.Receiver;
import system.Request;

/**
 * Данная команда завершает выполнение программы без сохранения
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ExitCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.exit(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "exit: ";
    }

    @Override
    public String getDescription() {
        return "close program without save";
    }
}
