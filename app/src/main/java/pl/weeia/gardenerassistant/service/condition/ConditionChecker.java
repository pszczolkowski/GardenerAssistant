package pl.weeia.gardenerassistant.service.condition;

import android.content.Context;
import android.util.Log;

import pl.weeia.gardenerassistant.service.weather.WeatherService;

public class ConditionChecker {

	private Context context;

	public ConditionChecker(Context context) {
		this.context = context;
	}

	public boolean check(String condition) {
		String[] parts = condition.split("<|>|<=|>=|==");
		String conditionType = parts[0].trim();
		ConditionOperator operator = fetchOperator(condition);
		float value = Float.parseFloat(parts[1]);

		Float parameter = findParameter(conditionType, operator);

		switch (operator) {
			case LESS_OR_EQUAL:
				return parameter <= value;
			case LESS_THAN:
				return parameter < value;
			case EQUAL:
				return parameter == value;
			case HIGHER_THAN:
				return parameter > value;
			case HIGHER_OR_EQUAL:
				return parameter >= value;
			default:
				throw new IllegalStateException("Unsupported operator <" + operator + ">");
		}
	}

	private ConditionOperator fetchOperator(String condition) {
		if (condition.contains("<=")) {
			return ConditionOperator.LESS_OR_EQUAL;
		} else if (condition.contains("<")) {
			return ConditionOperator.LESS_THAN;
		} else if (condition.contains("==")) {
			return ConditionOperator.EQUAL;
		} else if (condition.contains(">=")) {
			return ConditionOperator.HIGHER_OR_EQUAL;
		} else if (condition.contains(">=")) {
			return ConditionOperator.HIGHER_THAN;
		} else {
			throw new IllegalArgumentException("Unsuporrted operator in condition <" + condition + ">");
		}
	}

	private float findParameter(String conditionType, ConditionOperator operator) {
		switch (conditionType) {
			case "temperature":
				WeatherService weatherService = new WeatherService(context);

				if (operator == ConditionOperator.LESS_THAN || operator == ConditionOperator.LESS_OR_EQUAL) {
					if (!weatherService.hasStatus()) {
						return Float.MAX_VALUE;
					} else {
						return weatherService.getStatus().getMinTemperature();
					}
				} else {
					if (!weatherService.hasStatus()) {
						return Float.MIN_VALUE;
					} else {
						return new WeatherService(context).getStatus().getMinTemperature();
					}
				}
			default:
				throw new IllegalArgumentException("Invalid condition type <" + conditionType + ">");
		}
	}

}
