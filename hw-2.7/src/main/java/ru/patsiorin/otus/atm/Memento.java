package ru.patsiorin.otus.atm;

/**
 * Interface for objects that store snapshots of their origin classes states.
 */
public interface Memento {
    /**
     * Restores origin object's state to the stored in the snapshot.
     */
    void restore();
}
