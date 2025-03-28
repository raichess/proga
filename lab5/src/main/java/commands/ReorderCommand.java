package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import models.LabWork;

import java.util.Collections;
import java.util.Vector;

/**
 * Данная команда сортирует коллекцию в обратном порядке
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class ReorderCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            throw new WrongArgumentException(args[1]);
        }
        CollectionManager collectionManager = CollectionManager.getInstance();
        Vector<LabWork> labWorkCollection = collectionManager.getLabWorkCollection();
        Collections.reverse(labWorkCollection);
        collectionManager.setLabWorkCollection(labWorkCollection);
        System.out.println("Collection has been reordered in reverse order.");
    }

    @Override
    public String getName() {
        return "reorder: ";
    }

    @Override
    public String getDescription() {
        return "reorder the collection in reverse order.";
    }
}
