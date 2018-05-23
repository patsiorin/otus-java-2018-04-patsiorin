package ru.patsiorin.otus.atm.commands;

import ru.patsiorin.otus.atm.ATM;

import java.util.Scanner;

/**
 * The Command pattern's interface for menu commands.
 */
public interface Command {
    Scanner scanner = new Scanner(System.in);

    boolean execute(ATM atm);


    default String nextLine() {
        return scanner.nextLine();
    }

    default String getCashString(ATM atm) {
        return "Cash in ATM: " + atm.getAvailableCash();
    }

    /**
     * Command factory method.
     * @param commandStr
     * @return
     */
    static Command getCommand(String commandStr) {
        switch (commandStr) {
            case "dispense":
                return new DispenseCommand();
            case "deposit":
                return new DepositCommand();
            case "exit":
                return new ExitCommand();
            case "menu":
                return new MenuCommand();
            case "show":
                return new ShowCommand();
            default:
                return new UnknownCommand();
        }
    }
}
