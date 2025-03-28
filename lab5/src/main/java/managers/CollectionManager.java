package managers;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import exceptions.NoElementException;
import models.LabWork;
import models.Person;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Vector;

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

    public static CollectionManager getInstance() {
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
    public void addLabWork(LabWork labWork) {
        labWorkCollection.add(labWork);
    }

    /**
     * Очистить коллекцию
     */
    public void clearLabWorkCollection() {
        labWorkCollection.clear();
    }

    /**
     *  Показать все элементы коллекции
     */
    public void showLabWorkCollection() {
        for (LabWork labWork : labWorkCollection) {
            System.out.println(labWork);
        }
    }

    /**
     * Показать информацию о коллекции
     */
    public void infoLabWorkCollection() {
        System.out.println("Type: " + labWorkCollection.getClass() + "\n" +
                "Creation Date: " + getCreationDate() + "\n" + "Size: " + labWorkCollection.size());
    }

    /**
     * Удалить элемент из коллекции по его ID
     *
     * @param id
     * @return the lab work
     * @throws NoElementException исключение, возникающее при отсутствии элемента
     */
    public LabWork removeById(Long id) throws NoElementException {
        Iterator<LabWork> iterator = labWorkCollection.iterator();
        while (iterator.hasNext()) {
            LabWork labWork = iterator.next();
            if (labWork.getId().equals(id)) {
                iterator.remove();
                return labWork;
            }
        }
        throw new NoElementException(id);

    }

    /**
     * Удалить первый элемент коллекции
     *
     * @return the lab work
     * @throws NoElementException исключение, возникающее при отсутствии элемента
     */
    public LabWork removeFirst() throws NoElementException {
        if (labWorkCollection.isEmpty()) {
            throw new NoElementException("Collection is empty");
        }
        return labWorkCollection.remove(0);
    }

    /**
     * Удалить элемент по автору
     *
     * @param author автор
     * @return the lab work
     * @throws NoElementException исключение, возникающее при отсутствии элемента
     */
    public LabWork removeByAuthor(Person author) throws NoElementException {
        Iterator<LabWork> iterator = labWorkCollection.iterator();
        while (iterator.hasNext()) {
            LabWork labWork = iterator.next();
            if (author.equals(labWork.getAuthor())) {
                iterator.remove();
                return labWork;
            }
        }
        throw new NoElementException("no such author");
    }

    /**
     * Вывести количество элементов, значение поля author которых меньше заданного
     *
     * @param author автор
     * @return количество
     */
    public int countLessThanAuthor(Person author) {
        int count = 0;
        for (LabWork labWork : labWorkCollection) {
            if (labWork.getAuthor() != null && labWork.getAuthor().compareTo(author) < 0) {
                count++;
            }
        }
        return count;
    }


}
