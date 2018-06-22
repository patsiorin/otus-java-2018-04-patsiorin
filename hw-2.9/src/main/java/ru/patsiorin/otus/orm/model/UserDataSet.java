package ru.patsiorin.otus.orm.model;

import java.util.Objects;

/**
 * User data set. Inherits from DataSet
 */
public class UserDataSet extends DataSet {
    private String name;
    private int age;

    /**
     * Needed for reflective creation of this object.
     * @param id id in the database.
     */
    public UserDataSet(long id) {
        super(id);
    }

    public UserDataSet(long id, String name, int age) {
        super(id);
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, int age) {
        this(0L, name, age);
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + super.getId() + ", " +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataSet that = (UserDataSet) o;
        return age == that.age &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, age);
    }

    public void setName(String name) {
        this.name = name;
    }
}
