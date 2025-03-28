package models;

/**
 * Модель объекта "Сложность выполнения"
 *
 * @author raichess
 * @since 1.0
 */
public enum Difficulty {
    EASY,
    VERY_HARD,
    INSANE,
    HOPELESS,
    TERRIBLE;

    /**
     * Строковое представление сложности лабораторной работы
     *
     * @return строка
     */
    public static String names() {
        StringBuilder difficultyNameList = new StringBuilder();
        for (var difficulty : Difficulty.values()) {
            difficultyNameList.append(difficulty.name()).append(", ");
        }
        return difficultyNameList.substring(0, difficultyNameList.length() - 2);
    }

}
