package commands;

import exceptions.NoArgumentException;
import exceptions.NoElementException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;
import managers.Receiver;
import models.Country;
import models.Person;
import system.Request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Данная команда удаляет элемент из коллекции по его автору
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class RemoveByAuthorCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.removeByAuthor(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "remove_by_author: ";
    }

    @Override
    public String getDescription() {
        return "remove an element from the collection by its author (format: name,birthday,height,nationality).\n" +
                "nationality: " + Country.names();
    }

}
