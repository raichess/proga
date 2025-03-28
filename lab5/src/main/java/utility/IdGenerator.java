package utility;

import java.util.ArrayList;

/**
 * Класс генерирует ID для объекта Vehicle
 *
 * @author raichess
 * @see models.LabWork
 * @since 1.0
 */
public class IdGenerator {

    private static final Integer min = 10;
    private static final Integer max = 1000;
    private static ArrayList<Long> idList = new ArrayList<>();

    /**
     * Базовый конструктор
     *
     * @author raichess
     * @since 1.0
     */
    public IdGenerator() {
        idList = new ArrayList<>();
    }

    /**
     * Метод генерирует новый уникальный ID
     *
     * @author raichess
     * @since 1.0
     */
    public static Long generateId() {
        long id = (long) (Math.random() * (min - max + 1)) + min;
        while (idList.contains(id) || id <= 0) {
            id = (long) (Math.random() * (min - max + 1)) + min;
        }
        idList.add(id);
        return id;
    }

    /**
     * Метод проверяет ID на уникальность
     *
     * @param id какой id нужно проверить на уникальность
     * @author raichess
     * @since 1.0
     */
    public static boolean idUnique(long id) {
        if (idList.contains(id)) {
            return false;
        }
        return true;
    }

    /**
     * Добавляет ID в список
     *
     * @param id какой id нужно добавить
     * @author raichess
     * @since 1.0
     */
    public static void addId(long id) {
        idList.add(id);
    }

    /**
     * Удаляет ID из списка
     *
     * @param id какой id нужно удалить
     * @author raichess
     */
    public static void removeId(long id) {
        idList.remove(id);
    }

}
