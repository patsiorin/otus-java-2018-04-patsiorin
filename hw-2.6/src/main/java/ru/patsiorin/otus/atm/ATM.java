package ru.patsiorin.otus.atm;

import ru.patsiorin.otus.atm.commands.Command;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ATM Emulator class.
 */
public class ATM {
    private Map<Denomination, Cassette> cassettes = new TreeMap<>(Comparator.reverseOrder());

    public ATM() {
        init();
    }

    private void init() {
        for (Denomination denomination : Denomination.values()) {
            cassettes.put(denomination, new Cassette(denomination));
        }
    }

    /**
     * Starts the menu loop
     */
    public void start() {
        menu();
    }

    private void menu() {
        Command command = Command.getCommand("menu");
        while(command.execute(this)) {
            command = Command.getCommand(command.nextLine());
        }
    }

    /**
     * Returns the sum of all cash that is
     * inside the machine at the moment.
     *
     * @return cash sum that is currently in the ATM
     */
    public int getAvailableCash() {
        return cassettes.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue().getNoteCount())
                .sum();
    }

    /**
     * Returns a formatted String of all denominations and their respective counts
     * that are inside the ATM at the moment of calling.
     *
     * @return formatted String of denominations
     */
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
     * @param bankNoteStr String represeting the domination, e.g. 100, 500 etc.
     * @throws IllegalArgumentException if the domination for the input is not defined
     */
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

    public static void main(String[] args) {
        new ATM().start();
    }
}
