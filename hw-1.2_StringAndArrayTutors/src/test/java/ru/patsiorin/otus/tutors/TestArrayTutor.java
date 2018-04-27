package ru.patsiorin.otus.tutors;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestArrayTutor {
    private ArrayCopyTutor arrayCopyTutor;

    @Before
    public void setUp() {
        arrayCopyTutor = new ArrayCopyTutor();
        arrayCopyTutor.addAnimal("Лошадь");
        arrayCopyTutor.addAnimal("Носорог");
        arrayCopyTutor.addAnimal("Собака");
        arrayCopyTutor.addAnimal("Змея");
        arrayCopyTutor.addAnimal("Обезьяна");
        arrayCopyTutor.addAnimal("Индюк");
        arrayCopyTutor.addAnimal("Косуля");
        arrayCopyTutor.addAnimal("Лев");
        arrayCopyTutor.addAnimal("Тигр");
        arrayCopyTutor.addAnimal("Кошка");
        arrayCopyTutor.addAnimal("Черепаха");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDeleteAnimalNegativeIndex() {
        arrayCopyTutor.deleteAnimal(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDeleteAnimalIndexEqualsSize() {
        arrayCopyTutor.deleteAnimal(arrayCopyTutor.animals_size);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDeleteAnimalIndexGreaterThanSize() {
        arrayCopyTutor.deleteAnimal(arrayCopyTutor.animals_size + 1);
    }

    @Test
    public void testDeleteOneAnimal() {
        int size = arrayCopyTutor.animals_size;
        String expected = arrayCopyTutor.animals[1];
        arrayCopyTutor.deleteAnimal(0);
        assertEquals(size - 1, arrayCopyTutor.animals_size);
        assertEquals("New animal at position: "  + expected, expected, arrayCopyTutor.animals[0]);
    }

    @Test
    public void testDeleteTwoAnimals() {
        int size = arrayCopyTutor.animals_size;
        String expectedOne = arrayCopyTutor.animals[3];
        String expectedTwo = arrayCopyTutor.animals[4];
        arrayCopyTutor.deleteAnimal(1);
        arrayCopyTutor.deleteAnimal(1);
        assertEquals(size - 2, arrayCopyTutor.animals_size);
        assertEquals(arrayCopyTutor.animals[1], expectedOne);
        assertEquals(arrayCopyTutor.animals[2], expectedTwo);
    }

    @Test
    public void testDeleteLastAnimal() {
        int size = arrayCopyTutor.animals_size;
        String expectedLast = arrayCopyTutor.animals[arrayCopyTutor.animals_size - 2];
        arrayCopyTutor.deleteAnimal(size - 1);
        assertEquals(size - 1, arrayCopyTutor.animals_size);
        assertEquals(expectedLast, arrayCopyTutor.animals[arrayCopyTutor.animals_size - 1]);
    }

}
