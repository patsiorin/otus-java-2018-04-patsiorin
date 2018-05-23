package ru.patsiorin.otus.atm;

/**
 * The Cassette class is used to store
 * the number of banknotes in a cassette object.
 */
public class Cassette implements Comparable<Cassette> {
    private final Denomination denomination;
    private int noteCount;

    /**
     * Creates a cassette object for a domination
     * @param denomination
     */
    public Cassette(Denomination denomination) {
        this.denomination = denomination;
    }

    void depositNote() {
        noteCount++;
    }

    void dispenseNote(int number) {
        noteCount -= number;
    }

    int getNoteCount() {
        return noteCount;
    }

    Denomination getDenomination() {
        return denomination;
    }

    @Override
    public int compareTo(Cassette o) {
        return denomination.getValue() - o.denomination.getValue();
    }

    @Override
    public String toString() {
        return "Cassette{" +
                "newBankNote=" + denomination +
                ", noteCount=" + noteCount +
                '}';
    }
}
