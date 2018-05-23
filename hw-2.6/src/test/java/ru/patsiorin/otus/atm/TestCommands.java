package ru.patsiorin.otus.atm;

import ru.patsiorin.otus.atm.commands.*;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestCommands {
    @Test
    public void testDepositCommand() {
        Command command = Command.getCommand("deposit");
        assertThat(command, instanceOf(DepositCommand.class));
    }

    @Test
    public void testDispenseCommand() {
        Command command = Command.getCommand("dispense");
        assertThat(command, instanceOf(DispenseCommand.class));
    }

    @Test
    public void testExitCommand() {
        Command command = Command.getCommand("exit");
        assertThat(command, instanceOf(ExitCommand.class));
    }

    @Test
    public void testMenuCommand() {
        Command command = Command.getCommand("menu");
        assertThat(command, instanceOf(MenuCommand.class));
    }

    @Test
    public void testShowCommand() {
        Command command = Command.getCommand("show");
        assertThat(command, instanceOf(ShowCommand.class));
    }

    @Test
    public void testUnknownCommand() {
        Command command = Command.getCommand("anything");
        assertThat(command, instanceOf(UnknownCommand.class));
    }
}
