package ru.patsiorin.otus.atm.commands;

import ru.patsiorin.otus.atm.ATM;

public class MenuCommand implements Command {
    @Override
    public boolean execute(ATM atm) {
        System.out.println();
        System.out.println("ATM menu:");
        System.out.println("Type 'show' and press Enter to display available cash in the ATM");
        System.out.println("Type 'deposit'  and press Enter to add a banknote into the machine");
        System.out.println("Type 'dispense' and press Enter to withdraw cash");
        System.out.println("Type 'menu'     and press Enter to see this menu again");
        System.out.println("Type 'exit'     and press Enter to exit");
        System.out.println();
        return true;
    }
}
