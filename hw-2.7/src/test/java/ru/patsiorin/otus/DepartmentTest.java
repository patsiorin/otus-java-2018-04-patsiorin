package ru.patsiorin.otus;

import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.atm.ATM;
import ru.patsiorin.otus.atm.ATMConcrete;
import ru.patsiorin.otus.atm.Department;
import ru.patsiorin.otus.atm.DepartmentConcrete;

import static org.junit.Assert.assertEquals;

public class DepartmentTest {
    private Department department;

    @Before
    public void setUp() {
        this.department = new DepartmentConcrete();
    }

    @Test
    public void testGetBalanceSumFromAllATM() {
        ATM atm1 = new ATMConcrete();
        ATM atm2 = new ATMConcrete();
        ATM atm3 = new ATMConcrete();
        atm1.depositNote(100);
        atm2.depositNote(500);
        atm2.depositNote(1000);
        atm3.depositNote(5000);
        atm3.depositNote(100);
        atm3.depositNote(1000);

        department.registerATM(atm1);
        department.registerATM(atm2);
        department.registerATM(atm3);

        assertEquals(7700, department.getBalanceSumFromAllATM());
    }

    @Test
    public void restoreAllATMtoInitialState() {
        ATM atm1 = new ATMConcrete();
        ATM atm2 = new ATMConcrete();
        ATM atm3 = new ATMConcrete();
        atm1.depositNote(100);
        atm2.depositNote(500);
        atm2.depositNote(1000);
        atm3.depositNote(5000);
        atm3.depositNote(100);
        atm3.depositNote(1000);

        department.registerATM(atm1);
        department.registerATM(atm2);
        department.registerATM(atm3);

        atm1.depositNote(1000);
        atm1.dispenseSum(100);
        atm2.dispenseSum(1000);
        atm3.dispenseSum(5100);

        department.restoreAllATM();

        assertEquals(100, atm1.getAvailableCash());
        assertEquals(1500, atm2.getAvailableCash());
        assertEquals(6100, atm3.getAvailableCash());
    }
}
