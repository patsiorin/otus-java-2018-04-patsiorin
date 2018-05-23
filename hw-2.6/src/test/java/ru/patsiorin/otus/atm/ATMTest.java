package ru.patsiorin.otus.atm;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ATMTest {
    private ATM atm;

    @Before
    public void setUp() {
        atm = new ATM();
    }

    @Test
    public void testEmptyATM() {
        int cash = atm.getAvailableCash();
        assertEquals("Should be zero", 0, cash);
    }

    @Test
    public void testDepositBankNote() {
        atm.depositNote("100");
        assertEquals(100, atm.getAvailableCash());
        atm.depositNote("500");
        assertEquals(600, atm.getAvailableCash());
        atm.depositNote("5000");
        atm.depositNote("100");
        assertEquals(5700, atm.getAvailableCash());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDenomination() {
        atm.depositNote("300");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestedSumGreaterThanAvailable() {
        atm.depositNote("1000");
        atm.dispenseSum(1100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSumCantBeDispensed() {
        atm.depositNote("1000");
        for (int i = 0; i < 8; i++)
            atm.depositNote("100");
        atm.dispenseSum(900);
    }

    @Test
    public void testDispenseSum() {
        for(int i = 0; i < 4; i++) {
            atm.depositNote("100");
            atm.depositNote("500");
            atm.depositNote("1000");
            atm.depositNote("5000");
        }

        assertEquals(26400, atm.getAvailableCash());
        atm.dispenseSum(5000);
        assertEquals(21400, atm.getAvailableCash());
        atm.dispenseSum(100);
        assertEquals(21300, atm.getAvailableCash());
        atm.dispenseSum(15200);
        assertEquals(6100, atm.getAvailableCash());
        atm.dispenseSum(2000);
        assertEquals(4100, atm.getAvailableCash());
        atm.dispenseSum(4100);
        assertEquals(0, atm.getAvailableCash());
    }

    @Test
    public void testDispensedDenominations() {
        for (int i = 0; i < 5; i++) {
            atm.depositNote("100");
            atm.depositNote("500");
            atm.depositNote("1000");
            atm.depositNote("5000");
        }
        assertEquals(33000, atm.getAvailableCash());

        Map<Denomination, Integer> expected = new HashMap<>();
        expected.put(Denomination.newBankNote("5000"), 3);
        expected.put(Denomination.newBankNote("1000"), 2);
        expected.put(Denomination.newBankNote("500"), 1);
        expected.put(Denomination.newBankNote("100"), 4);

        Map<Denomination, Integer> result = atm.dispenseSum(17900);

        assertThat(result.size(), is(4));
        assertThat(result, is(expected));

    }

}
