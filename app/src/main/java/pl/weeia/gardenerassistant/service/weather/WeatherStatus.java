package pl.weeia.gardenerassistant.service.weather;

public class WeatherStatus {

	private float minTemperature;

	protected WeatherStatus() {}

	public WeatherStatus(float minTemperature) {
		this.minTemperature = minTemperature;
	}

	public float getMinTemperature() {
		return minTemperature;
	}

}
