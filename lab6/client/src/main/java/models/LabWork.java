package models;

import java.io.*;

/**
 * Модель объекта "Лабораторная работа"
 * Содержит геттеры/сеттеры каждого поля класса
 * Некоторые поля имеют ограничения. Они подписаны
 *
 * @author raichess
 * @since 1.0
 */
public class LabWork implements Comparable<LabWork>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Поле не может быть null, Значение поля должно быть больше 0,
     * Значение этого поля должно быть уникальным,
     * Значение этого поля должно генерироваться автоматически
     *
     * @since 1.0
     */
    private Long id;
    /**
     * Название лабораторной работы
     * Поле не может быть null, Строка не может быть пустой
     *
     * @since 1.0
     */
    private String name;
    /**
     * Координаты
     * Поле не может быть null
     *
     * @see Coordinates
     * @since 1.0
     */
    private Coordinates coordinates;
    /**
     * Дата создания
     * Поле не может быть null, Значение этого поля должно генерироваться автоматически
     *
     * @since 1.0
     */
    private java.time.ZonedDateTime creationDate;
    /**
     * Минимальное количество баллов
     * Значение поля должно быть больше 0
     *
     * @since 1.0
     */
    private long minimalPoint;
    /**
     * Описание работы
     * Поле не может быть null
     *
     * @since 1.0
     */
    private String description;
    /**
     * Сложность работы
     * Поле может быть null
     *
     * @see Difficulty
     * @since 1.0
     */
    private Difficulty difficulty;
    /**
     * Автор работы
     * Поле не может быть null
     *
     * @see Person
     * @since 1.0
     */
    private Person author;


    /**
     * Базовый пустой конструктор
     *
     * @since 1.0
     */
    public LabWork() {
    }

    /**
     * Конструктор с заданными полями
     *
     * @since 1.0
     */
    public LabWork(Long id, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, long minimalPoint, String description, Difficulty difficulty, Person author) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.difficulty = difficulty;
        this.author = author;

    }

    public LabWork(String[] data) throws Exception {
        this.id = 0L;
        this.name = data[0];
        this.coordinates = new Coordinates(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        this.creationDate = java.time.ZonedDateTime.parse(data[3]);
        this.minimalPoint = Long.parseLong(data[4]);
        this.description = data[5];
        this.difficulty = Difficulty.valueOf(data[6]);
        this.author = new Person(data[7], java.time.LocalDate.parse(data[8]), Double.parseDouble(data[9]), Country.valueOf(data[10]));
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.time.ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public long getMinimalPoint() {
        return minimalPoint;
    }

    public String getDescription() {
        return description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdForUpdate(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(java.time.ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setMinimalPoint(long minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public int compareTo(LabWork o) {
        return Long.compare(o.getId(), this.getId());
    }

    @Override
    public String toString() {
        String result = String.format("Id: %d\n" + "Name: %s\n" + "Coordinates: {x: %d, y: %.2f}\n" + "Creation Date: %s\n" + "Minimal Point: %d\n" + "Description: %s\n" + "Difficulty: %s\n" + "Author: %s\n",
                getId(), getName(), getCoordinates().getX(), getCoordinates().getY(), getCreationDate(), getMinimalPoint(), getDescription(), getDifficulty(), getAuthor());
        return result;
    }
}

