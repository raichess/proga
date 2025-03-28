package commands;

import exceptions.WrongArgumentException;
import managers.CommandManager;

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
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        String[] sp = new String[6];
        int n = 0;
        for (Command command : CommandManager.lastSixCommand) {
            sp[n] = command.getName();
            n += 1;
        }
        for (int i = sp.length - 1; i >= 0; i--) {
            if (sp[i] != null) {
                System.out.println("-" + sp[i]);
            }
        }
        if (CommandManager.lastSixCommand.isEmpty()) {
            System.out.println("history is empty");
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
