package pl.weeia.gardenerassistant.service.weather;

import org.bitpipeline.lib.owm.WeatherStatusResponse;

public class WeatherLoadResult {

	private WeatherStatusResponse weatherStatusResponse;
	private Exception exception;

	public WeatherLoadResult(WeatherStatusResponse weatherStatusResponse) {
		this.weatherStatusResponse = weatherStatusResponse;
	}

	public WeatherLoadResult(Exception exception) {
		this.exception = exception;
	}

	public boolean hasSucceeded() {
		return weatherStatusResponse != null;
	}

	public WeatherStatusResponse getWeatherStatusResponse() {
		if (weatherStatusResponse == null) {
			throw new IllegalStateException("Weather status has not been loaded successfully");
		}

		return weatherStatusResponse;
	}

	public Exception getException() {
		if (exception == null) {
			throw new IllegalStateException("There has been no exception");
		}

		return exception;
	}
}
