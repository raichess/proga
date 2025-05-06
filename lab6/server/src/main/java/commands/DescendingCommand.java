package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.Receiver;
import models.LabWork;
import system.Request;

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
    public String execute(Request request) throws Exception {
        try {
            return Receiver.sortDescending(request);
        } catch (Exception e) {
            return e.getMessage();
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
