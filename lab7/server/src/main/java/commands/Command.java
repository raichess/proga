package commands;

import system.Request;

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
     * @return
     * @author raichess
     * @since 1.0
     */
    public String execute(Request request) throws Exception;

    public String getName();

    public String getDescription();
}
