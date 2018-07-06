package ru.patsiorin.otus.orm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User data set. Inherits from DataSet
 */
@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {
    private String name;
    private int age;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userDataSet")
    private List<PhoneDataSet> phones = new ArrayList<>();

    public UserDataSet() {
    }

    /**
     * Needed for reflective creation of this object.
     * @param id id in the database.
     */
    public UserDataSet(long id) {
        this.setId(id);
    }

    public UserDataSet(long id, String name, int age) {
        this.setId(id);
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, int age) {
        this(-1, name, age);
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

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
        for(PhoneDataSet phone : phones) {
            phone.setUserDataSet(this);
        }
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }
}
