package commands;

import exceptions.NoArgumentException;
import exceptions.NoElementException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;
import utility.IdGenerator;

/**
 * Данная команда удаляет элемент из коллекции по его ID
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class RemoveByIdCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        try {
            CollectionManager collectionManager = CollectionManager.getInstance();
            if (args.length > 2) {
                throw new WrongArgumentException(args[2]);
            }
            if (args.length < 2) {
                throw new NoArgumentException("id");
            }
            collectionManager.removeById(Long.parseLong(args[1]));
            IdGenerator.removeId(Long.parseLong(args[1]));
            System.out.println("Removing complete");
        } catch (NoElementException e) {
            System.out.println(e.getMessage());
            System.out.println("No such element");
        } catch (NumberFormatException e) {
            System.out.println("Please enter digit");
        }
    }

    @Override
    public String getName() {
        return "remove_by_id id: ";
    }

    @Override
    public String getDescription() {
        return "remove element by id";
    }
}
