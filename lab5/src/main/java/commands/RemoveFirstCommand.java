package commands;

import exceptions.NoElementException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;

/**
 * Данная команда удаляет первый элемент из коллекции
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class RemoveFirstCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception{
        try {
            CollectionManager collectionManager = CollectionManager.getInstance();
            if (args.length > 1) {
                throw new WrongArgumentException(args[1]);
            }
            collectionManager.removeFirst();
        } catch (NoElementException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Removed the first element");
    }
    @Override
    public String getName() {
        return "remove_first: ";
    }

    @Override
    public String getDescription() {
        return "remove first element ";
    }
}
