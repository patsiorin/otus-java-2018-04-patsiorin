package ru.patsiorin.otus.atm;

import java.util.Map;

public interface ATM extends MementoOriginator{
    int getAvailableCash();

    String getAllDenominationsCountString();

    void depositNote(int nominal);

    Map<Denomination, Integer> dispenseSum(int sum);
}
