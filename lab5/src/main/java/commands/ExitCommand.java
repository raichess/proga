package commands;

import exceptions.WrongArgumentException;

/**
 * Данная команда завершает выполнение программы без сохранения
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ExitCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        System.exit(1);
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
