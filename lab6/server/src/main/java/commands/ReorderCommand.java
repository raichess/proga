package commands;

import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.Receiver;
import models.LabWork;
import system.Request;

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
    public String execute(Request request) throws Exception {
        try {
            return Receiver.reorder(request);
        } catch (Exception e) {
            return e.getMessage();
        }
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
