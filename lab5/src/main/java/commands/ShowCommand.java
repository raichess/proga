package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;

/**
 * Данная команда выводит все элементы коллекции
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ShowCommand implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        CollectionManager collectionManager = CollectionManager.getInstance();
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        collectionManager.showLabWorkCollection();
        if (collectionManager.getLabWorkCollection().isEmpty()) {
            System.out.println("There are no lab work collection");
        }
    }

    @Override
    public String getName() {
        return "show: ";
    }

    @Override
    public String getDescription() {
        return "show data";
    }
}
