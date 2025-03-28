package commands;

/**
 * Интерфейс для реализации команд.
 *
 * @author raichess
 * @since 1.0
 */
public interface Command {

    /**
     * Базовый метод для вывода исполнения команды
     * Выбрасывает ошибки при реализации
     *
     * @author raichess
     * @since 1.0
     */
    public void execute(String[] args) throws Exception;

    public String getName();

    public String getDescription();
}
