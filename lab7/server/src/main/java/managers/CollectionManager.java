package managers;

import DBlogic.PostgreSQLManager;
import exceptions.NoElementException;
import models.LabWork;
import system.Request;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Класс отвечает за взаимодействие с коллекцией на базовом уровне
 *
 * @author raichess
 * @see LabWork
 * @since 1.0
 */
public class CollectionManager {
    private static volatile CollectionManager instance;
    private static Vector<LabWork> labWorkCollection;
    private static final ReentrantLock lock = new ReentrantLock();
    private final ZonedDateTime creationDate;

    /**
     * Базовый конструктор
     *
     * @since 1.0
     */
    private CollectionManager() {
        this.labWorkCollection = new Vector<>();
        this.creationDate = ZonedDateTime.now();
    }

    public static CollectionManager getInstance() {
        CollectionManager localInstance = instance;
        if (localInstance == null) {
            synchronized (CollectionManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CollectionManager();
                }
            }
        }
        return localInstance;
    }
    public void loadCollectionFromDB() {
        lock.lock();
        try {
            PostgreSQLManager manager = new PostgreSQLManager();
            Vector<LabWork> labWorks = manager.getCollectionFromDB();

            if (labWorks == null) {
                throw new RuntimeException("Failed to load collection from DB");
            }

            labWorkCollection.clear();
            labWorkCollection.addAll(labWorks);
            Logger.getLogger(CollectionManager.class.getName()).info("Collection reloaded from DB");
        } catch (Exception e) {
            Logger.getLogger(CollectionManager.class.getName())
                    .severe("Error loading collection: " + e.getMessage());
            throw new RuntimeException("DB load failed", e);
        } finally {
            lock.unlock();
        }
    }
    public void writeCollectionToDB() {
        PostgreSQLManager dbmanager = new PostgreSQLManager();
        dbmanager.writeCollectionToDB();
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
        lock.lock();
        try {
            return new Vector<>(labWorkCollection);
        } finally {
            lock.unlock();
        }
    }



    /**
     * Показать все элементы коллекции
     *
     * @return
     */
    public static String showCollection() {
        if (labWorkCollection.isEmpty()) {
            return "Collection is empty";
        }

        String result = labWorkCollection.stream()
                .map(LabWork::toString)
                .collect(Collectors.joining("\n"));
        return result;
    }


    /**
     * Вывести количество элементов, значение поля author которых меньше заданного
     *
     * @return количество
     */

    public static String countLessThanAuthor(Request request) {
        lock.lock();
        try {
            String enterAuthor = request.getKey();
            Vector<LabWork> labWorkCollection = CollectionManager.getInstance().getLabWorkCollection();
            long count = labWorkCollection.stream()
                    .filter(labWork -> !labWork.getAuthor().equals(enterAuthor))
                    .count();
            return "Do not match: " + count;
        } finally {
            lock.unlock();
        }
    }

    public static String descending() {
        lock.lock();
        Vector<LabWork> labWorkCollection = CollectionManager.getInstance().getLabWorkCollection();
        try {
            if (labWorkCollection.isEmpty()) {
                return "Collection is empty, nothing to sort.";
            }

            Vector<LabWork> copy = new Vector<>(labWorkCollection);
            copy.sort((lab1, lab2) -> Long.compare(lab2.getId(), lab1.getId()));

            lock.lock();
            try {
                labWorkCollection.clear();
                labWorkCollection.addAll(copy);
                return "Collection sorted in descending order by ID.";
            } finally {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }
    public static String reorder() throws NoElementException {
        lock.lock();
        Vector<LabWork> labWorkCollection = CollectionManager.getInstance().getLabWorkCollection();
        try {
            if (labWorkCollection.isEmpty()) {
                throw new NoElementException("Collection is empty");
            }

            Vector<LabWork> copy = new Vector<>(labWorkCollection);
            Collections.reverse(copy);

            lock.lock(); // Двойная блокировка для записи
            try {
                labWorkCollection.clear();
                labWorkCollection.addAll(copy);
                return "Collection has been reordered in reverse";
            } finally {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }

}
