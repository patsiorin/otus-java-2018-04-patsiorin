package ru.patsiorin.otus.atm;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete implementation of Department interface
 */
public class DepartmentConcrete implements Department {
    private final Map<ATM, Memento> map = new HashMap<>();

    @Override
    public void registerATM(ATM atm) {
        if (map.containsKey(atm))
            throw new IllegalArgumentException("ATM already registered");
        map.put(atm, atm.save());
    }

    @Override
    public void deregisterATM(ATM atm) {
        if (!map.containsKey(atm))
            throw new IllegalArgumentException("No such ATM registered");
        map.remove(atm);
    }

    @Override
    public int getBalanceSumFromAllATM() {
        return map.keySet().stream().mapToInt(ATM::getAvailableCash).sum();
    }

    @Override
    public void restoreAllATM() {
        for (Map.Entry<ATM, Memento> entry : map.entrySet()) {
            entry.getValue().restore();
        }
    }
}
