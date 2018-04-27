package ru.patsiorin.otus.tutors;

public class StringTutor  {

	private static final int MIN_NAME_LENGTH = 3;
	
	/**
	 *  Убедитесь, что приветствие greeting имеет вид
	 *  Привет, Иван Иванов!
	 *  или
	 *  Привет,Петр Первый!
	 *  т.е. начинается на Привет, заканчивается на !
	 *  и содержит 2 слова для имени и фамилии,
	 *  причем имя и фамилия не короче 3 букв
	 *  и начинаются с большой буквы
	 */
	public boolean checkGreeting(String greeting) {
		if (greeting == null) return false;

		if (!greeting.startsWith("Привет,") || !greeting.endsWith("!")) {
			return false;
		}

		String[] nameParts = greeting
				.substring(greeting.indexOf(',') + 1, greeting.length() - 1).trim()
				.split(" ");

		if (nameParts.length < 2) return false;

		for (int i = 0; i < 2; i++) {
			String namePart = nameParts[i].trim();
			if (!Character.isUpperCase(namePart.charAt(0)) || namePart.length() < MIN_NAME_LENGTH)
				return false;
		}

		return true;
	}
}
