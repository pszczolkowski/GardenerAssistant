package pl.weeia.gardenerassistant.service.weather;

import android.os.AsyncTask;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

import java.io.IOException;

public class WeatherLoadAsyncTask extends AsyncTask<Void, Void, WeatherLoadResult> {

	private String cityName;
	private String countryCode;
	private WeatherLoadListener weatherLoadListener;

	public WeatherLoadAsyncTask(String cityName, String countryCode, WeatherLoadListener weatherLoadListener) {
		this.cityName = cityName;
		this.countryCode = countryCode;
		this.weatherLoadListener = weatherLoadListener;
	}

	@Override
	protected WeatherLoadResult doInBackground(Void... params) {
		OwmClient owm = new OwmClient();
		try {
			WeatherStatusResponse weatherStatusResponse = owm.currentWeatherAtCity(cityName, countryCode);
			return new WeatherLoadResult(weatherStatusResponse);
		} catch (IOException | JSONException exception) {
			return new WeatherLoadResult(exception);
		}
	}

	@Override
	protected void onPostExecute(WeatherLoadResult weatherLoadResult) {
		weatherLoadListener.onWeatherLoad(weatherLoadResult);
	}
}
