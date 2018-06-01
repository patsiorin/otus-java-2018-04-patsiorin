package ru.patsiorin.otus;

import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.atm.ATM;
import ru.patsiorin.otus.atm.ATMConcrete;
import ru.patsiorin.otus.atm.Memento;

import static org.junit.Assert.assertEquals;

public class MementoTest {
    private ATM atm;
    @Before
    public void setUp() {
        this.atm = new ATMConcrete();
    }

    @Test
    public void testNewOATMMemento() {
        Memento memento = atm.save();
        atm.depositNote(1000);
        assertEquals(1000, atm.getAvailableCash());
        memento.restore();
        assertEquals(0, atm.getAvailableCash());
    }

    @Test
    public void testATMWithMondyMemento() {
        atm.depositNote(100);
        atm.depositNote(1000);
        atm.depositNote(5000);
        assertEquals(6100, atm.getAvailableCash());
        Memento memento = atm.save();
        atm.depositNote(500);
        atm.depositNote(100);
        atm.dispenseSum(5100);
        assertEquals(1600, atm.getAvailableCash());
        memento.restore();
        assertEquals(6100, atm.getAvailableCash());
    }
}
