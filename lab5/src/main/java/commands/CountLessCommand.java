package commands;

import exceptions.NoArgumentException;
import exceptions.WrongArgumentException;
import managers.CollectionManager;
import models.Country;
import models.Person;

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
    public void execute(String[] args) throws Exception {
        CollectionManager collectionManager = CollectionManager.getInstance();
        if (args.length > 2) {
            throw new WrongArgumentException(args[2]);
        }
        if (args.length < 2) {
            throw new NoArgumentException("author");
        }
        String[] authorData = args[1].split(","); // Формат: name,birthday,height,nationality
        if (authorData.length != 4) {
            throw new WrongArgumentException("Author data must be in format: name,birthday,height,nationality");
        }

        String name = authorData[0].trim();
        LocalDate birthday;
        try {
            birthday = LocalDate.parse(authorData[1].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new WrongArgumentException("Invalid date format. Use YYYY-MM-DD.");
        }
        Double height = authorData[2].trim().isEmpty() ? null : Double.parseDouble(authorData[2].trim());
        Country nationality = authorData[3].trim().isEmpty() ? null : Country.valueOf(authorData[3].trim().toUpperCase());

        Person author = new Person(name, birthday, height, nationality);

        int count = collectionManager.countLessThanAuthor(author);
        System.out.println("Number of elements with author less than " + author + ": " + count);
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
