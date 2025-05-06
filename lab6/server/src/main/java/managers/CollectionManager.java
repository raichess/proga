package managers;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import exceptions.NoElementException;
import models.LabWork;
import models.Person;
import system.Request;
import utility.IdGenerator;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс отвечает за взаимодействие с коллекцией на базовом уровне
 *
 * @author raichess
 * @see LabWork
 * @since 1.0
 */
public class CollectionManager {
    @XStreamImplicit
    private static Vector<LabWork> labWorkCollection;
    private final ZonedDateTime creationDate;
    private static CollectionManager instance;

    /**
     * Базовый конструктор
     *
     * @since 1.0
     */
    private CollectionManager() {
        labWorkCollection = new Vector<>();
        creationDate = ZonedDateTime.now();
    }

    public static synchronized CollectionManager getInstance() {
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }


    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Получить коллекцию
     *
     * @return коллекция со всеми элементами
     */
    public static Vector<LabWork> getLabWorkCollection() {
        return labWorkCollection;
    }

    /**
     * Установить коллекцию
     *
     * @param labWorkCollection коллекция
     */
    public void setLabWorkCollection(Vector<LabWork> labWorkCollection) {
        CollectionManager.labWorkCollection = labWorkCollection;
    }

    /**
     * Добавить элемент коллекции
     *
     * @param labWork элемент коллекции
     */
    public static void addLabWork(LabWork labWork) {
        labWorkCollection.add(labWork);
    }

    /**
     * Очистить коллекцию
     */
    public static void clearLabWorkCollection() {
        labWorkCollection.clear();
    }

    /**
     * Показать все элементы коллекции
     *
     * @return
     */
    public String showLabWorkCollection() {
        if (labWorkCollection.isEmpty()) {
            return "collection is empty";
        }
        String result = labWorkCollection.stream().map(labWork -> labWork.toString()).collect(Collectors.joining("\n"));
        return result;
    }

    /**
     * Удалить элемент из коллекции по его ID
     *
     * @return the lab work
     * @throws NoElementException исключение, возникающее при отсутствии элемента
     */
    public static String removeById(Request request) throws NoElementException {
        if (labWorkCollection.isEmpty()) {
            return "collection is empty";
        }
        long id = Long.parseLong(request.getKey());
        Optional<LabWork> optionalLabWork = labWorkCollection.stream()
                .filter(labWork -> labWork.getId() == id).findFirst();
        if (optionalLabWork.isPresent()) {
            labWorkCollection.remove(optionalLabWork.get());
            IdGenerator.removeId(id);
            return "element removed";
        } else {
            throw new NoElementException(id);
        }
    }

    /**
     * Удалить первый элемент коллекции
     *
     * @return the lab work
     * @throws NoElementException исключение, возникающее при отсутствии элемента
     */
    public static String removeFirst() throws NoElementException {
        if (labWorkCollection.isEmpty()) {
            throw new NoElementException("Collection is empty");
        }
        LabWork removed = labWorkCollection.remove(0);
        IdGenerator.removeId(removed.getId());
        return "First element removed: " + removed.getName();
    }

    /**
     * Удалить элемент по автору
     *
     * @return the lab work
     * @throws NoElementException исключение, возникающее при отсутствии элемента
     */
    public static String removeByAuthor(Request request) throws NoElementException  {
        if (labWorkCollection.isEmpty()) {
            return "Collection is empty";
        }
        String author = request.getKey(); // В поле key будет передано имя автора
        Optional<LabWork> toRemove = labWorkCollection.stream()
                .filter(lw -> lw.getAuthor() != null && lw.getAuthor().getName().equals(author))
                .findFirst();

        if (toRemove.isPresent()) {
            labWorkCollection.remove(toRemove.get());
            IdGenerator.removeId(toRemove.get().getId());
            return "Element with author '" + author + "' removed";
        } else {
            throw new NoElementException(author);
        }
    }

    /**
     * Вывести количество элементов, значение поля author которых меньше заданного
     *
     * @return количество
     */
    public static String countLessThanAuthor(Request request) throws NoElementException {
        Vector<LabWork> labWorkCollection = CollectionManager.getLabWorkCollection();
        String enterAuthor = request.getKey();
        long count = labWorkCollection.stream().filter(labWork -> !(labWork.getAuthor().equals(enterAuthor))).count();
        return ("Do not match: " + count);
    }

    public static String updateId(Request request) {
        try {
            Vector<LabWork> vehicleCollection = CollectionManager.getLabWorkCollection();
            long inputEl = Long.parseLong(request.getKey());
            if (vehicleCollection.isEmpty()) {
                return "Collection is empty";
            }

            Optional<LabWork> labWorkToUpdate = labWorkCollection.stream()
                    .filter(labWork -> labWork.getId() == inputEl)
                    .findFirst();

            if (!labWorkToUpdate.isPresent()) {
                return "No element with id " + inputEl + " found in the collection";
            }

            LabWork updatedLabwork = request.getLabWork();
            updatedLabwork.setIdForUpdate(inputEl);
            updatedLabwork.setCreationDate(labWorkToUpdate.get().getCreationDate());
            removeById(request);
            addLabWork(updatedLabwork);
            return "Labwork with id " + inputEl + " was successfully updated";
        }catch (NumberFormatException e) {
            return "Please enter digit";
        }catch (NoElementException e) {
            return e.getMessage();
        }
    }

    public static String descending() {
        if (labWorkCollection.isEmpty()) {
            return "Collection is empty, nothing to sort.";
        }
        labWorkCollection.sort((lab1, lab2) -> Long.compare(lab2.getId(), lab1.getId()));
        return "Collection sorted in descending order by ID.";
    }

    public static String reorder() throws NoElementException {
        if (labWorkCollection.isEmpty()) {
            throw new NoElementException("Collection is empty");
        }
        Collections.reverse(labWorkCollection);
        return "Collection has been reordered in reverse";
    }



}
