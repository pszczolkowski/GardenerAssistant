package pl.weeia.gardenerassistant.service.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitpipeline.lib.owm.StatusWeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;

import java.io.File;
import java.io.IOException;

public class WeatherService {

	private static final String FILE_NAME = "weather_status_store";

	private Context context;
	private WeatherStatus weatherStatus;

	public WeatherService(Context context) {
		this.context = context;

		loadWeatherStatusIfNecessary();
	}

	private void loadWeatherStatusIfNecessary() {
		if (weatherStatus == null) {
			File file = new File(context.getFilesDir(), FILE_NAME);
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				weatherStatus = objectMapper.readValue(file, WeatherStatus.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void refresh () {
		if (thereIsNoInternetConnection()) {
			return;
		}

		new WeatherLoaderImpl().load("Lodz", "PL", new WeatherLoadListener() {
			@Override
			public void onWeatherLoad(WeatherLoadResult weatherLoadResult) {
				if (!weatherLoadResult.hasSucceeded()) {
					weatherLoadResult.getException().printStackTrace();
					return;
				}

				WeatherStatusResponse weatherStatusResponse = weatherLoadResult.getWeatherStatusResponse();
				if (weatherStatusResponse.hasWeatherStatus()) {
					float minTemperature = Float.MAX_VALUE;

					for (StatusWeatherData statusWeatherData : weatherStatusResponse.getWeatherStatus()) {
						if (!statusWeatherData.getMain().hasTempMin()) {
							continue;
						}

						float temperature = statusWeatherData.getMain().getTempMin();
						if (temperature < minTemperature) {
							minTemperature = temperature;
						}
					}

					weatherStatus = new WeatherStatus(kelvinToCelsius(minTemperature));
					saveWeatherStatus();
				}
			}
		});
	}

	private boolean thereIsNoInternetConnection() {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			return netInfo == null || !netInfo.isConnectedOrConnecting();
	}

	private float kelvinToCelsius(float temperature) {
		return temperature - 273.15f;
	}

	private void saveWeatherStatus() {
		try {
			File file = new File(context.getFilesDir(), FILE_NAME);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(file, weatherStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WeatherStatus getStatus() {
		return weatherStatus;
	}

	public boolean hasStatus() {
		return weatherStatus != null;
	}
}
