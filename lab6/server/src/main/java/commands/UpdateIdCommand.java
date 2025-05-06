package commands;

import exceptions.NoArgumentException;
import exceptions.NoElementException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.Receiver;
import models.LabWork;
import system.Request;

import java.time.ZonedDateTime;
import java.util.Vector;

/**
 * Данная команда обновляет значения полей элемента по его ID
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class UpdateIdCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.updateId(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "updateId {element}: ";
    }

    @Override
    public String getDescription() {
        return "update element";
    }
}
