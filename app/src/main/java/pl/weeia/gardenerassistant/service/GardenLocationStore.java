package pl.weeia.gardenerassistant.service;

import android.content.Context;
import android.content.SharedPreferences;

import pl.weeia.gardenerassistant.model.GardenLocation;

public class GardenLocationStore {

	private static final String SHARED_PREFERENCES_NAME = "preferences";
	private static final String SHARED_PREFERENCES_GARDE_LOCATION_CITY_NAME = "garden_location_city_name";
	private static final String SHARED_PREFERENCES_GARDE_LOCATION_COUNTRY_CODE = "garden_location_county_code";
	private static final String DEFAULT_CITY_NAME = "Lodz";
	private static final String DEFAULT_COUNTRY_CODE = "PL";

	private Context context;
	private GardenLocation gardenLocation;

	public GardenLocationStore(Context context) {
		this.context = context;

		loadGardenLocationIfNecessary();
	}

	private void loadGardenLocationIfNecessary() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		String cityName = sharedPreferences.getString(SHARED_PREFERENCES_GARDE_LOCATION_CITY_NAME, DEFAULT_CITY_NAME);
		String countryCode = sharedPreferences.getString(SHARED_PREFERENCES_GARDE_LOCATION_COUNTRY_CODE, DEFAULT_COUNTRY_CODE);

		if (cityName != null && countryCode != null) {
			gardenLocation = new GardenLocation(cityName, countryCode);
		}
	}

	public GardenLocation get() {
		return gardenLocation;
	}

	public void set(GardenLocation gardenLocation) {
		this.gardenLocation = gardenLocation;
		saveGardenLocation();
	}

	private void saveGardenLocation() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		sharedPreferences
			.edit()
			.putString(SHARED_PREFERENCES_GARDE_LOCATION_CITY_NAME, gardenLocation.getCityName())
			.putString(SHARED_PREFERENCES_GARDE_LOCATION_COUNTRY_CODE, gardenLocation.getCountryCode())
			.apply();
	}

}
