package ru.patsiorin.otus.atm.commands;

import ru.patsiorin.otus.atm.ATM;
import ru.patsiorin.otus.atm.Denomination;

import java.util.Arrays;

public class DepositCommand implements Command {
    @Override
    public boolean execute(ATM atm) {
        System.out.println();
        System.out.println("Available denominations: " + getAvailableDenominationsString());
        System.out.println("Type in the banknote's denomination to deposit or any other string to return to the main menu");
        try {
            while (true) {
                System.out.println(getCashString(atm));
                int nominal = Integer.valueOf(nextLine());
                atm.depositNote(nominal);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to the main menu");
        }
        System.out.println();
        return true;
    }

    private String getAvailableDenominationsString() {
        return Arrays.stream(Denomination.values())
                .map(d -> String.valueOf(d.getValue()))
                .reduce((a, b) -> a + ", " + b)
                .orElseThrow(() -> new RuntimeException("Can't happen"));
    }
}
