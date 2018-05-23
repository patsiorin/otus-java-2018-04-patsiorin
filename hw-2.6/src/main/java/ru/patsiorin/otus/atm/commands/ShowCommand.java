package ru.patsiorin.otus.atm.commands;

import ru.patsiorin.otus.atm.ATM;

public class ShowCommand implements Command {
    @Override
    public boolean execute(ATM atm) {
        System.out.println();
        System.out.println(getCashString(atm));
        System.out.println(atm.getAllDenominationsCountString());
        return true;
    }
}
