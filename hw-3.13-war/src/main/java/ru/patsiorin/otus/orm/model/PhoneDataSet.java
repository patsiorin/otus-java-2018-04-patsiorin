package ru.patsiorin.otus.orm.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet {
    private String number;
    @ManyToOne
    private UserDataSet userDataSet;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public void setUserDataSet(UserDataSet userDataSet) {
        this.userDataSet = userDataSet;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(userDataSet, that.userDataSet);
    }

    @Override
    public int hashCode() {

        return Objects.hash(number, userDataSet);
    }
}
