package pl.weeia.gardenerassistant.util;

import java.util.Calendar;

public class DateUtil {

	private DateUtil() {}

	public static boolean isBefore(Calendar first, Calendar second) {
		if (first.get(Calendar.YEAR) < second.get(Calendar.YEAR)) {
			return true;
		} else {
			return first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
				first.get(Calendar.DAY_OF_YEAR) < second.get(Calendar.DAY_OF_YEAR);
		}
	}

	public static boolean areEqual(Calendar first, Calendar second) {
		return first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
			first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR);
	}

	public static boolean isToday(Calendar calendar) {
		return areEqual(calendar, Calendar.getInstance());
	}

	public static boolean isTomorrow(Calendar calendar) {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		return areEqual(calendar, tomorrow);
	}
}
