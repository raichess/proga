package managers;

import exceptions.WrongArgumentException;
import models.*;
import utility.Validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * Класс генерирует объект Labwork
 *
 * @author raichess
 * @see LabWork
 * @since 1.0
 */
public class LabWorkAsker {
    /**
     * Метод поэтапно запрашивает данные и проверяет их для создания объекта
     *
     * @return элемент коллекции
     */
    public static LabWork createLabWork() {
        Long id = 0L;
        String input, x, y;
        Coordinates coordinates;
        Scanner scanner = new Scanner(System.in);
        LabWork labWork;
        labWork = new LabWork();
        labWork.setId(id);
        labWork.setCreationDate(ZonedDateTime.now());
        Person person;
        person = new Person();

        while (true) {
            try {
                System.out.print("Input author name: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "Name");
                person.setName(input);
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be empty or null");
            }
        }

        while (true) {
            try {
                System.out.print("Input birthday as yyyy-MM-dd: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "birthday");
                Validator.birthdayGood(input);
                person.setBirthday(LocalDate.parse(input));
                break;
            } catch (DateTimeException | WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be empty or null, please enter yyyy-MM-dd");
            }
        }
        while (true) {
            try {
                System.out.print("Input height: ");
                input = scanner.nextLine();
                Validator.heightGood(input);
                person.setHeight(Double.parseDouble(input));
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " must be a positive");
            }
        }
        while (true) {
            try {
                System.out.print("Input nationality from " + Country.names() + ": ");
                input = scanner.nextLine().trim().toUpperCase();
                Validator.countryGood(input);
                person.setNationality(Country.valueOf(input));
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot have a value other than " + Country.names());
            }
        }
        labWork.setAuthor(person);


        while (true) {
            try {
                System.out.print("Input labwork name: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "Name");
                labWork.setName(input);
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be empty or null");
            }
        }
        while (true) {
            try {
                System.out.print("Input x: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "x");
                Validator.xGood(input);
                x = input;
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be empty and must be an integer");
            } catch (NumberFormatException e) {
                System.out.println("Please enter integer digit");
            }
        }
        while (true) {
            try {
                System.out.print("Input y: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "y");
                Validator.yGood(input);
                y = input;
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be more then 793, empty and must be an integer");
            } catch (NumberFormatException e) {
                System.out.println("Please enter integer digit less then 793");
            }
        }
        coordinates = new Coordinates(Integer.parseInt(x), Double.parseDouble(y));
        labWork.setCoordinates(coordinates);

        while (true) {
            try {
                System.out.print("Input minimal point: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "minimal point");
                Validator.minimalPointGood(input);
                labWork.setMinimalPoint(Integer.valueOf(input));
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be less then 0, empty and must be an integer");
            } catch (NumberFormatException e) {
                System.out.println("Please enter integer digit more then 0");
            }
        }
        while (true) {
            try {
                System.out.print("Input difficulty type from " + Difficulty.names() + ": ");
                input = scanner.nextLine().trim().toUpperCase();
                Validator.inputNotEmpty(input, "difficulty type");
                Validator.difficultyGood(input);
                labWork.setDifficulty(Difficulty.valueOf(input));
                break;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot have a value other than " + Difficulty.names());
            }
        }
        while (true) {
            try {
                System.out.print("Input description: ");
                input = scanner.nextLine();
                Validator.inputNotEmpty(input, "Description");
                labWork.setDescription(input);
                System.out.println("Generating...ready");
                return labWork;
            } catch (WrongArgumentException e) {
                System.out.println(e.getMessage() + " cannot be empty or null");
            }
        }


    }

}
