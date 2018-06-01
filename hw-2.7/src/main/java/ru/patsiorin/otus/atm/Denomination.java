package ru.patsiorin.otus.atm;

/**
 * The denomination enum is used to create
 * unique denomination and keep their values.
 */
public enum Denomination {
    ONE_HUNDRED(100), FIVE_HUNDRED(500), ONE_THOUSAND(1_000), FIVE_THOUSAND(5_000);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    /**
     * A factory method that creates denominations from user input strings.
     *
     * @param nominal user input
     * @return Denomination
     * @throws IllegalArgumentException if no denomination exists for the provided string.
     */
    public static Denomination newBankNote(int nominal) {
        for (Denomination d : values()) {
            if (nominal == d.value) {
                return d;
            }
        }
        throw new IllegalArgumentException("Denomination " + nominal + "does not exist");
    }

    /**
     * Returns the value of the denomination.
     * @return value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
