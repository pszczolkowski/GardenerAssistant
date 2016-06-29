package pl.weeia.gardenerassistant.util;

import java.util.Calendar;

public class YearlessDate {

	private Calendar calendar;

	public YearlessDate(int day, int month) {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
	}

	public boolean isBefore(YearlessDate yearlessDate) {
		return isBefore(yearlessDate.calendar);
	}

	public boolean isBefore(Calendar calendar) {
		return this.calendar.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR);
	}

	public boolean isAfterOrEqual(Calendar calendar) {
		return this.calendar.get(Calendar.DAY_OF_YEAR) >= calendar.get(Calendar.DAY_OF_YEAR);
	}

	public boolean isBeforeOrEqual(Calendar calendar) {
		return this.calendar.get(Calendar.DAY_OF_YEAR) <= calendar.get(Calendar.DAY_OF_YEAR);
	}
}
