package ru.patsiorin.otus.atm.commands;

import ru.patsiorin.otus.atm.ATM;
import ru.patsiorin.otus.atm.Denomination;

import java.util.Map;

public class DispenseCommand implements Command {
    @Override
    public boolean execute(ATM atm) {
        System.out.println();
        System.out.println(getCashString(atm));
        System.out.println("Enter sum to dispense:");
        try {
            Map<Denomination, Integer> dispensed = atm.dispenseSum(Integer.valueOf(nextLine()));
            System.out.println("Your cash: " + getDispensedCashString(dispensed));
            System.out.println(getCashString(atm));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to the main menu");
        }
        System.out.println();
        return true;
    }

    private String getDispensedCashString(Map<Denomination, Integer> combinations) {
        return combinations.entrySet().stream()
                        .map(entry -> entry.getValue() + "x" + entry.getKey().toString())
                        .reduce((a, b) -> a + ", " + b).orElse("None");
    }
}
