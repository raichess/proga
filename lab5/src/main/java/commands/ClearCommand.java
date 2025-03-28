package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;

/**
 * Данная команда очищает коллекцию
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ClearCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        CollectionManager collectionManager = CollectionManager.getInstance();
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        if (collectionManager.getLabWorkCollection().isEmpty()) {
            System.out.println("Collection already cleared");
        } else {
            collectionManager.clearLabWorkCollection();
            System.out.println("Collection cleared");
        }
    }

    @Override
    public String getName() {
        return "clear: ";
    }

    @Override
    public String getDescription() {
        return "clears the collection";
    }
}
