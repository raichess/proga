package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.LabWorkAsker;

/**
 * Данная команда добавляет новый элемент в коллекцию
 *
 *  @see Command
 *  @author raichess
 *  @since 1.0
 */
public class AddCommand implements Command {

    @Override
    public void execute(String[] args) throws Exception{
        LabWorkAsker labWorkAsker = new LabWorkAsker();
        CollectionManager collectionManager = CollectionManager.getInstance();
        if (args.length > 1 ) {
            throw new WrongArgumentException(args[1]);
        }
        collectionManager.addLabWork(labWorkAsker.createLabWork());
        System.out.println("Lab work added");
    }
    @Override
    public String getName() {
        return "add {element}: ";
    }

    @Override
    public String getDescription() {
        return "add new vehicle in collection";
    }
}
