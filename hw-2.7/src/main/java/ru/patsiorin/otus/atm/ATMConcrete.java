package ru.patsiorin.otus.atm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ATMConcrete Emulator class.
 */
public class ATMConcrete implements ATM, MementoOriginator {
    private Map<Denomination, Cassette> cassettes;

    public ATMConcrete() {
        init();
    }

    private void init() {
        cassettes = new TreeMap<>(Comparator.reverseOrder());
        for (Denomination denomination : Denomination.values()) {
            cassettes.put(denomination, new Cassette(denomination));
        }
    }

    /**
     * Returns the sum of all cash that is
     * inside the machine at the moment.
     *
     * @return cash sum that is currently in the ATMConcrete
     */
    @Override
    public int getAvailableCash() {
        return cassettes.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue().getNoteCount())
                .sum();
    }

    /**
     * Returns a formatted String of all denominations and their respective counts
     * that are inside the ATMConcrete at the moment of calling.
     *
     * @return formatted String of denominations
     */
    @Override
    public String getAllDenominationsCountString() {
        return cassettes.entrySet().stream()
                .filter(e -> e.getValue().getNoteCount() > 0)
                .map(e -> e.getKey() + ": " + e.getValue().getNoteCount())
                .reduce((a, b) -> a + ", " + b).orElse("Nothing yet");
    }

    /**
     * Gets the domination enum from user input
     * and inserts it to the appropriate cassette.
     *
     * @param nominal String represeting the domination, e.g. 100, 500 etc.
     * @throws IllegalArgumentException if the domination for the input is not defined
     */
    @Override
    public void depositNote(int nominal) {
        Denomination note = Denomination.newBankNote(nominal);
        cassettes.get(note).depositNote();
    }

    /**
     * Finds the denominations needed to dispense the sum with the minimum amount of banknotes
     * and, if successful, removes them from the cassettes.
     *
     * @param sum Sum to be dispesed
     *
     * @return A map representation of dispensed denominations and their counts in the reveresed sorting order.
     * @throws IllegalArgumentException if the sum cannot be dispensed (no cash, not divisible by gcd or a combination of denominations cannot be combined.
     */
    @Override
    public Map<Denomination, Integer> dispenseSum(int sum) {
        Map<Denomination, Integer> combinations = combineSumFromAvailableNotes(sum);
        for (Map.Entry<Denomination, Integer> entry : combinations.entrySet()) {
            dispenseNumberOfNotes(entry.getKey(), entry.getValue());
        }
       return combinations;
    }

    private Map<Denomination, Integer> combineSumFromAvailableNotes(int sum) {
        if (sum > getAvailableCash()) throw new IllegalArgumentException("Not enough cash");
        int gcd = Util.gcd(getIntegerArrayOfDenominationValues());
        if (sum % gcd != 0) {
            throw new IllegalArgumentException("Request sum must be divisible by " + gcd);
        }

        List<Cassette> cassettesList = getSortedListOfNonEmptyCassettes();
        Map<Denomination, Integer> banknotes = new TreeMap<>(Comparator.reverseOrder());
        for (Cassette c : cassettesList) {
            int bankNoteValue = c.getDenomination().getValue();
            if (sum >= bankNoteValue) {
                int nBanknotes = sum / bankNoteValue;
                if (nBanknotes > c.getNoteCount()) {
                    nBanknotes = c.getNoteCount();
                }
                banknotes.put(c.getDenomination(), nBanknotes);
                sum -= nBanknotes * bankNoteValue;
            }
        }

        if (sum != 0) {
            throw new IllegalArgumentException(sum + " can't be comprised of available denominations");
        }

        return banknotes;
    }

    private void dispenseNumberOfNotes(Denomination denomination, int numberOfNotes) {
        Cassette cassette = cassettes.get(denomination);
        if (cassette.getNoteCount() - numberOfNotes >= 0) {
            cassette.dispenseNote(numberOfNotes);
        } else {
            throw new IllegalStateException("No notes of this denomination are available");
        }
    }

    private List<Cassette> getSortedListOfNonEmptyCassettes() {
        return cassettes.values().stream()
                .filter(c -> c.getNoteCount() > 0)
                .collect(Collectors.toList());
    }

    private Integer[] getIntegerArrayOfDenominationValues() {
        return Arrays.stream(Denomination.values()).map(Denomination::getValue).toArray(Integer[]::new);
    }

   @Override
   public Memento save() {
        return new MementoConcrete(this);
   }

    public class MementoConcrete implements Memento {
        private ATMConcrete originator;
        private Set<Cassette> state = new HashSet<>();

        public MementoConcrete(ATMConcrete originator) {
            this.originator = originator;
            for (Map.Entry<Denomination, Cassette> entry : originator.cassettes.entrySet()) {
                state.add(new Cassette(entry.getValue()));
            }
        }

        public void restore() {
            originator.init();
            for (Cassette cassette : state) {
                originator.cassettes.put(cassette.getDenomination(), cassette);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATMConcrete that = (ATMConcrete) o;
        return Objects.equals(cassettes, that.cassettes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cassettes);
    }
}
