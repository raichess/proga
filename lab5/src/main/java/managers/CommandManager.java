package managers;

import commands.Command;
import commands.*;
import exceptions.UnknownCommandException;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;

/**
 * Класс обеспечивает связь между командами и CollectionManager
 *
 * @author raichess
 * @see CollectionManager
 * @since 1.0
 */
public class CommandManager {
    private static LinkedHashMap<String, Command> commandList;
    public static ArrayDeque<Command> lastSixCommand = new ArrayDeque<>();

    public CommandManager() {
        commandList = new LinkedHashMap<>();
        commandList.put("help", new HelpCommand());
        commandList.put("exit", new ExitCommand());
        commandList.put("info", new InfoCommand());
        commandList.put("show", new ShowCommand());
        commandList.put("add", new AddCommand());
        commandList.put("updateId", new UpdateIdCommand());
        commandList.put("remove_by_id", new RemoveByIdCommand());
        commandList.put("clear", new ClearCommand());
        commandList.put("save", new SaveCommand());
        commandList.put("execute_script", new ExecuteScriptCommand());
        commandList.put("remove_first", new RemoveFirstCommand());
        commandList.put("reorder", new ReorderCommand());
        commandList.put("history", new HistoryCommand());
        commandList.put("remove_by_author", new RemoveByAuthorCommand());
        commandList.put("count_less_than_author", new CountLessCommand());
        commandList.put("print_descending", new DescendingCommand());

    }

    /**
     * Выполняет команду, сохраняя ее имя
     *
     * @param line строка команды
     * @since 1.0
     */
    public static void startExecute(String line) throws Exception{
        String commandName = line.split(" ")[0];
        if (!commandList.containsKey(commandName)) {
            throw new UnknownCommandException(commandName);
        }
        Command command = commandList.get(commandName);
        command.execute(line.split(" "));
        if (lastSixCommand.size() == 6) {
            lastSixCommand.removeLast();
        } else {
            lastSixCommand.addFirst(command);
        }
    }

    /**
     * Получить список команд
     *
     * @return список команд
     */
    public LinkedHashMap<String, Command> getCommandList() {
        return commandList;
    }

}
