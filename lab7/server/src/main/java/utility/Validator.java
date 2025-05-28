package utility;

import models.LabWork;

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
