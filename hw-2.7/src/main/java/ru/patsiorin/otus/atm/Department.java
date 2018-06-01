package ru.patsiorin.otus.atm;

/**
 *  Department interface for manipulation ATMs.
 *
 */
public interface Department {
    /**
     * Registers ATMs in the department
     * and stores a copy of its state at the moment of registering.
     * @param atm ATM to register
     * @throws IllegalArgumentException if provided ATM is already registered
     */
    void registerATM(ATM atm) throws IllegalArgumentException;

    /**
     * Deregisteres ATMs from the department.
     * @param atm ATM to be removed from the department
     * @throws IllegalArgumentException if provided ATM is not present in the department/
     */
    void deregisterATM(ATM atm) throws IllegalArgumentException;

    /**
     * @return sum of balances of every registered ATM
     */
    int getBalanceSumFromAllATM();

    /**
     * Resets all ATMs states to the state they were in at the moment of registering
     */
    void restoreAllATM();
}
