package managers;

import commands.*;
import exceptions.UnknownCommandException;
import system.Request;

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
     * @return
     * @since 1.0
     */
    public static String startExecute(Request request) throws Exception {
        String commandName = request.getMessage().split(" ")[0];
        if (!commandList.containsKey(commandName)) {
            throw new UnknownCommandException(commandName);
        }
        Command command = commandList.get(commandName);
        String message = command.execute(request);
        if (lastSixCommand.size() >= 6) {
            lastSixCommand.removeLast();
        } else {
            lastSixCommand.addFirst(command);
        }
        return message;
    }

    public static String startExecutingClientMode(Request request) {
        try {
            if(!request.getMessage().equals("exit") && !request.getMessage().equals("save")) {
                return startExecute(request);
            }
            return "Unknown command";
        }catch (Exception e) {
            return e.getMessage();
        }
    }



    public static void startExecutingServerMode(Request request) {
        try {
            if (request.getMessage().equals("exit") || request.getMessage().equals("save")) {
                startExecute(request);
            }
        } catch (Exception e) {
            System.out.println("Something wrong with initializing of server");
        }
    }


    /**
     * Получить список команд
     *
     * @return список команд
     */
    public static LinkedHashMap<String, Command> getCommandList() {
        return commandList;
    }

}
