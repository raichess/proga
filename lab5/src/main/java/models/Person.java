package models;


import utility.IdGenerator;

import java.time.LocalDate;
import java.util.Objects;


/**
 * Модель объекта "Автор"
 * Содержит геттеры/сеттеры каждого поля класса
 * Некоторые поля имеют ограничения. Они подписаны
 *
 * @author raichess
 * @since 1.0
 */
public class Person implements Comparable<Person> {
    /**
     * Поле не может быть null,
     * Строка не может быть пустой
     *
     * @since 1.0
     */
    private String name;
    /**
     * Поле не может быть null,
     *
     * @since 1.0
     */
    private LocalDate birthday;
    /**
     * Поле может быть null,
     * Значение поля должно быть больше 0
     *
     * @since 1.0
     */
    private Double height;
    /**
     * Поле может быть null,
     *
     * @since 1.0
     */
    private Country nationality;

    /**
     * Базовый пустой конструктор
     *
     * @since 1.0
     */
    public Person() {
    }

    /**
     * Конструктор с заданными полями
     *
     * @since 1.0
     */
    public Person(String name, LocalDate birthday, Double height, Country nationality) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(birthday, person.birthday) &&
                Objects.equals(height, person.height) &&
                nationality == person.nationality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthday, height, nationality);
    }

    @Override
    public int compareTo(Person other) {
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) return nameComparison;

        int birthdayComparison = this.birthday.compareTo(other.birthday);
        if (birthdayComparison != 0) return birthdayComparison;

        int heightComparison = Double.compare(
                this.height == null ? 0 : this.height,
                other.height == null ? 0 : other.height
        );
        if (heightComparison != 0) return heightComparison;

        return this.nationality.compareTo(other.nationality);
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Birthday: %s, Height: %.2f, Nationality: %s",
                name, birthday, height, nationality != null ? nationality : "Unknown");
    }


}
