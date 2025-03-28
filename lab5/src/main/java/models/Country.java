package models;

/**
 * Модель объекта "Национальность"
 *
 * @author raichess
 * @since 1.0
 */
public enum Country {

    UNITED_KINGDOM,

    CHINA,

    INDIA,

    SOUTH_KOREA,

    NORTH_KOREA;

    /**
     * Строкове представление списка стран
     *
     * @return строка
     */
    public static String names() {
        StringBuilder countryNameList = new StringBuilder();
        for (var country : values()) {
            countryNameList.append(country.name()).append(", ");
        }
        return countryNameList.substring(0, countryNameList.length() - 2);
    }
}
