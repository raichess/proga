package commands;

import exceptions.NoArgumentException;
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
 * Данная команда сравнивает элементы по автору
 *
 * @see Command
 * @author raichess
 * @since 1.0
 */
public class CountLessCommand implements Command {
    @Override
    public String execute(Request request) throws Exception {
        try {
            return Receiver.countByAuthor(request);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "count_less_than_author: ";
    }

    @Override
    public String getDescription() {
        return "count the number of elements whose author is less than the specified one (format: name,birthday,height,nationality)";
    }
}
