package pl.weeia.gardenerassistant.service.weather;

public interface WeatherLoader {

	void load(String cityName, String countryCode, WeatherLoadListener weatherLoadListener);

}
