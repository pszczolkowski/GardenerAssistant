package pl.weeia.gardenerassistant.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

import pl.weeia.gardenerassistant.util.YearlessDate;

public class Period {

	private YearlessDate from;
	private YearlessDate to;

	@JsonCreator
	public Period(@JsonProperty("from") String from, @JsonProperty("to") String to) {
		this.from = stringToDate(from);
		this.to = stringToDate(to);
	}

	private YearlessDate stringToDate(String string) {
		String[] stringParts = string.split("\\.");
		return new YearlessDate(Integer.parseInt(stringParts[0], 10), Integer.parseInt(stringParts[1], 10));
	}
}
