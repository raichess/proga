package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.DumpManager;

import static managers.Console.filepath;

/**
 * Данная команда сохраняет коллекцию в XML файл
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class SaveCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        CollectionManager collectionManager = CollectionManager.getInstance();
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        DumpManager dumpManager = DumpManager.getInstance(filepath);
        dumpManager.writeLabWorks(collectionManager.getLabWorkCollection());
        System.out.println("saved successfully");
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
