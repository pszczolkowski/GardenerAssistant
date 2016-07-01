package pl.weeia.gardenerassistant.service.weather;

public class WeatherLoaderImpl implements WeatherLoader {

	public void load(String cityName, String countryCode, WeatherLoadListener weatherLoadListener) {
		new WeatherLoadAsyncTask(cityName, countryCode, weatherLoadListener)
			.execute();
	}

}
