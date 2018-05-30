package ru.patsiorin.otus.atm;

import ru.patsiorin.otus.atm.commands.Command;

public class App {
    public static void main(String[] args) {
        ATM atm = new ATM();
        Command command = Command.getCommand("menu");
        while(command.execute(atm)) {
            command = Command.getCommand(command.nextLine());
        }
    }
}
