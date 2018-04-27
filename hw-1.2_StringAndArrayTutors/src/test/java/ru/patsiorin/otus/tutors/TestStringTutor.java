package ru.patsiorin.otus.tutors;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class TestStringTutor {
    private StringTutor stringTutor;
    @Before
    public void setUp() {
        stringTutor = new StringTutor();
    }

    @Test
    public void testCheckGreetingWithNormalString() {
        assertTrue(stringTutor.checkGreeting("Привет, Иван Иванов!"));
        assertTrue(stringTutor.checkGreeting("Привет, Петр Первый!"));
    }

    @Test
    public void testCheckGreetingWithoutSpaceBeforeName() {
        assertTrue(stringTutor.checkGreeting("Привет,Петр Первый!"));
    }

    @Test
    public void testCheckGreetingWithSpaceBeforeExclamation() {
        assertTrue("Пробел перед восклицательным знаком ни на что не влияет", stringTutor.checkGreeting(
                "Привет, Петр Первый !"));
    }

    @Test
    public void testCheckGreetingFirstWordWrong() {
        assertFalse("В начале должно быть слово Привет и запятая",
                stringTutor.checkGreeting("Здравствуйте, Петр Первый!"));
    }

    @Test
    public void testCheckGreetingWithoutExclamation() {
        assertFalse("В конце должен быть восклицательный знак",
                stringTutor.checkGreeting("Привет, Петр Первый"));
    }

    @Test
    public void testCheckGreetingShortName() {
        assertFalse("Имя слишком короткое",
                stringTutor.checkGreeting("Привет, Ли Сунь!"));
    }

    @Test
    public void testCheckGreetingShortFname() {
        assertFalse("Фамилия слишком короткая",
                stringTutor.checkGreeting("Привет, Куй Ли!"));
    }

    @Test
    public  void testCheckGreetingOneName() {
        assertFalse("Должны быть указаны и имя, и фамилия",
                stringTutor.checkGreeting("Привет, Петр!"));
    }

    @Test
    public void testCheckGreetingCapitalName() {
        assertFalse("Первая буква имени должна быть заглавной",
                stringTutor.checkGreeting("Привет, петр Первый!"));
    }

    @Test
    public void testCheckGreetingCapitalFname() {
        assertFalse("Первая буква фамилии должна быть заглавной",
                stringTutor.checkGreeting("Привет, Петр первый!"));
    }

}
