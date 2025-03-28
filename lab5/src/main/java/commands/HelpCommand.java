package commands;

import exceptions.WrongArgumentException;
import managers.CommandManager;

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
    public void execute(String[] args) throws Exception {
        CommandManager commandManager = new CommandManager();
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        LinkedHashMap<String, Command> commandList = commandManager.getCommandList();
        for (String commandName : commandList.keySet()) {
            Command command = commandList.get(commandName);
            System.out.println(command.getName() + " " + command.getDescription());
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
