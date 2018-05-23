package ru.patsiorin.otus.atm.commands;

import ru.patsiorin.otus.atm.ATM;

public class ExitCommand implements Command {

    @Override
    public boolean execute(ATM atm) {
        System.out.println();
        System.out.println("Exiting ATM... Bye.");
        return false;
    }
}
