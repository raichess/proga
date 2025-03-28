package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import models.LabWork;

import java.util.Collections;
import java.util.Vector;

/**
 * Данная команда сортирует элементы по убыванию
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class DescendingCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        CollectionManager collectionManager = CollectionManager.getInstance();
        Vector<LabWork> labWorkCollection = collectionManager.getLabWorkCollection();
        Vector<LabWork> sortedCollection = new Vector<>(labWorkCollection);
        Collections.sort(sortedCollection);
        collectionManager.setLabWorkCollection(sortedCollection);

        if (sortedCollection.isEmpty()) {
            System.out.println("The collection is empty.");
        } else {
            System.out.println("Elements in descending order (sorted by id):");
            for (LabWork labWork : sortedCollection) {
                System.out.println(labWork);
            }
        }
    }

    @Override
    public String getName() {
        return "print_descending: ";
    }

    @Override
    public String getDescription() {
        return "print the elements of the collection in descending order (sorted by id) and save the new order";
    }
}
