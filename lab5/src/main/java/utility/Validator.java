package utility;

import exceptions.WrongArgumentException;
import models.Country;
import models.Difficulty;
import models.LabWork;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Vector;

/**
 * Класс для проверки на валидность полей объекта LabWork и для проверки входных данных из файла
 *
 * @author raichess
 * @since 1.0
 */
public class Validator {
    Vector<LabWork> labWorksList;

    public Validator(Vector<LabWork> labWorksList) {
        this.labWorksList = labWorksList;
    }

    /**
     * Проверяет, что значение строки не null
     *
     * @param arg  аргумент строки
     * @param data что из Organization мы проверяем
     * @throws WrongArgumentException если значение arg null
     */
    public static void inputNotEmpty(String arg, String data) throws WrongArgumentException {
        if (arg.isEmpty() || arg.trim().isEmpty()) {
            throw new WrongArgumentException(data);
        }
    }
    /**
     * Проверяет корректность координат по X
     *
     * @param args аргумент строки
     * @throws WrongArgumentException если координата некорректна
     * @see models.Coordinates
     */
    public static void xGood(String args) throws WrongArgumentException {
        int x = Integer.parseInt(args);
        if (x >= 2147483647) {
            throw new WrongArgumentException("x");
        }
    }
    /**
     * Проверяет корректность координат по Y
     *
     * @param args аргумент строки
     * @throws WrongArgumentException если координата некорректна
     * @see models.Coordinates
     */
    public static void yGood(String args) throws WrongArgumentException {
        double y = Double.parseDouble(args);
        if (y >= 793) {
            throw new WrongArgumentException("y");
        }
    }
    /**
     * Проверяет корректность minimalpoint
     *
     * @param args аргумент строки
     * @throws WrongArgumentException если некорректно
     * @see models.LabWork
     */
    public static void minimalPointGood(String args) throws WrongArgumentException {
        long minimalPoint = Long.parseLong(args);
        if (minimalPoint <= 0) {
            throw new WrongArgumentException("minimalPoint");
        }
    }

    public static void countryGood(String args) throws WrongArgumentException {
        try {
            Country.valueOf(args.toUpperCase());
        } catch (Exception e) {
            throw new WrongArgumentException("country");
        }
    }

    public static void difficultyGood(String args) throws WrongArgumentException {
        try {
            Difficulty.valueOf(args.toUpperCase());
        } catch (Exception e) {
            throw new WrongArgumentException("difficulty");
        }
    }


    public static void birthdayGood(String args) throws WrongArgumentException {
        try {
            LocalDate.parse(args);
        } catch (Exception e) {
            throw new WrongArgumentException("birthday");
        }
    }

    public static void heightGood(String args) throws WrongArgumentException {
        double height = Double.parseDouble(args);
        if (height <= 0) {
            throw new WrongArgumentException("height");
        }
    }

    public Vector<LabWork> validateFile() {
        for (Iterator<LabWork> iterator = labWorksList.iterator(); iterator.hasNext(); ) {
            LabWork labWork = iterator.next();
            if ((labWork.getId() > 0) && (IdGenerator.idUnique(labWork.getId()))) {
                IdGenerator.addId(labWork.getId());
            } else {
                iterator.remove();
                System.out.println("id must be greater than 0, unique and as an integer, element was removed ");
            }
            if (labWork.getName() == null || labWork.getName().trim().isEmpty()) {
                iterator.remove();
                System.out.println("name cannot be empty or null, element was removed ");
            }
            if (labWork.getCoordinates() == null) {
                iterator.remove();
                System.out.println("coordinates cannot be empty or null, element was removed ");
            }
            if (labWork.getMinimalPoint() <= 0) {
                iterator.remove();
                System.out.println("minimalPoint must be greater than 0, element was removed ");
            }
            if (labWork.getDescription() == null || labWork.getDescription().trim().isEmpty()) {
                iterator.remove();
                System.out.println("description cannot be empty or null, element was removed ");
            }
            if (labWork.getAuthor().getName() == null || labWork.getAuthor().getName().trim().isEmpty()) {
                iterator.remove();
                System.out.println("author name cannot be empty or null, element was removed ");
            }
            if (labWork.getAuthor().getBirthday() == null) {
                iterator.remove();
                System.out.println("birthday cannot be empty or null, element was removed ");
            }
            if (labWork.getAuthor().getHeight() <= 0) {
                iterator.remove();
                System.out.println("height must be greater than 0, element was removed ");
            }
            if (labWork.getAuthor() == null) {
                iterator.remove();
                System.out.println("author cannot be empty or null, element was removed ");
            }
            if (labWork.getCoordinates().getY() >= 793) {
                iterator.remove();
                System.out.println("y must be less than 793, element was removed ");
            }
        }
        return labWorksList;
    }


}
