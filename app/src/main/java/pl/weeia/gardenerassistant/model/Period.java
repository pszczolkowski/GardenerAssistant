package pl.weeia.gardenerassistant.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.Validate;

import java.util.Calendar;

import pl.weeia.gardenerassistant.util.YearlessDate;

public class Period {

	private YearlessDate from;
	private YearlessDate to;

	@JsonCreator
	public Period(@JsonProperty("from") String from, @JsonProperty("to") String to) {
		Validate.matchesPattern(from, "^[0-3][0-9]\\.[0-3][0-9]$", "Invalid date format");
		Validate.matchesPattern(to, "^[0-3][0-9]\\.[0-3][0-9]$", "Invalid date format");

		this.from = stringToDate(from);
		this.to = stringToDate(to);
	}

	private YearlessDate stringToDate(String string) {
		String[] stringParts = string.split("\\.");
		return new YearlessDate(Integer.parseInt(stringParts[0], 10), Integer.parseInt(stringParts[1], 10));
	}

	public boolean startsBefore(Period period) {
		return from.isBefore(period.from);
	}

	public boolean isBetween(Calendar start, Calendar end) {
		if (from.isBefore(start)) {
			return to.isAfterOrEqual(start);
		} else {
			return from.isBeforeOrEqual(end);
		}
	}

	public boolean contains(Calendar date) {
		return from.isBeforeOrEqual(date) &&
			to.isAfterOrEqual(date);
	}

	public YearlessDate getStart() {
		return from;
	}

	public YearlessDate getEnd() {
		return to;
	}
}
