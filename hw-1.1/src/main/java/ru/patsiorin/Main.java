package ru.patsiorin;

import com.google.common.base.Joiner;

/**
 * Otus Java Developer course homework 1.1
 * This class and its dependencies (Google Guava) are build with Maven
 *
 * @author Valerii Patsiorin
 */
public class Main {
    /**
     * Entry point of the application.
     * Makes use of Google Guava's Joiner helper class
     * to join String arguments from the standard input
     * and writes the result to the standard output.
     * In case no input arguments are provided
     * throws IllegalArgumentException
     *
     * @param args input strings to join
     * @throws IllegalArgumentException
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments supplied");
        }

        Joiner joiner = Joiner.on("*");
        System.out.println(joiner.join(args));
    }
}
