package models;


/**
 * Модель объекта "Координаты"
 * Содержит геттеры/сеттеры каждого поля класса
 * Некоторые поля имеют ограничения
 *
 * @author raichess
 * @since 1.0
 */
public class Coordinates {
    /**
     * Координата X
     */
    private Integer x;
    /**
     * Координата Y
     * Значение поля должно быть меньше 793
     */
    private double y;

    /**
     * Базовый конструктор
     *
     * @param x x
     * @param y y
     */
    public Coordinates(Integer x, double y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


}
