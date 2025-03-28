package managers;

import models.LabWork;

import java.io.File;
import java.util.Scanner;
import java.io.InputStream;
import java.util.Vector;

/**
 * Класс отвечает за чтение командной строки
 * Обеспечивает связь между пользователем и командами
 *
 * @see CommandManager
 * @author raichess
 * @since 1.0
 */
public class Console {

    public static String filepath;

    /**
     * Выполнение команды из InputStream
     *
     * @param input откуда происходит чтение
     */
    public void start(InputStream input) {
        Scanner scanner = new Scanner(input);
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = CollectionManager.getInstance();
        filepath = System.getenv("LABWORKS_FILEPATH");
        if (filepath == null || filepath.isEmpty()) {
            System.out.println("Ошибка: Переменная окружения LABWORKS_FILEPATH не задана.");
            System.exit(1);
        }

        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("Ошибка: Файл по указанному пути не существует.");
            System.exit(1);
        }

        System.out.println("Запущено. Введите команду (наберите 'help' для списка команд):");

        if (file.length() == 0) {
            collectionManager.setLabWorkCollection(new Vector<LabWork>());
        } else {
            try {
                DumpManager dumpManager = DumpManager.getInstance(filepath);
                collectionManager.setLabWorkCollection(dumpManager.readLabWorks());
                System.out.println("Данные успешно загружены.");
            } catch (Exception e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
                System.exit(1);
            }
        }

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (!command.isEmpty() && command.charAt(0) == '\uFEFF') {
                command = command.substring(1);
            }
            if (!command.isEmpty()) {
                try {
                    commandManager.startExecute(command);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}
