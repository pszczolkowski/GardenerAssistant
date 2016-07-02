package pl.weeia.gardenerassistant.activity.locationchoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.model.GardenLocation;
import pl.weeia.gardenerassistant.service.GardenLocationStore;

public class LocationChoiceActivity extends AppCompatActivity {

	private GardenLocationStore gardenLocationStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_choice);

		gardenLocationStore = new GardenLocationStore(this);

		final EditText cityNameEditText = (EditText) findViewById(R.id.gardenLocationCityNameEditText);
		final EditText countryCodeEditText = (EditText) findViewById(R.id.gardenLocationCountryCodeEditText);

		cityNameEditText.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable editable) {
				setCityName(editable.toString());
			}
		});

		countryCodeEditText.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable editable) {
				setCountryCode(editable.toString());
			}
		});

		GardenLocation gardenLocation = gardenLocationStore.get();
		cityNameEditText.setText(gardenLocation.getCityName());
		countryCodeEditText.setText(gardenLocation.getCountryCode());
	}

	private void setCityName(String cityName) {
		GardenLocation gardenLocation = gardenLocationStore.get();
		gardenLocationStore.set(new GardenLocation(stripAccentCharacters(cityName), gardenLocation.getCountryCode()));
	}

	private void setCountryCode(String countryCode) {
		GardenLocation gardenLocation = gardenLocationStore.get();
		gardenLocationStore.set(new GardenLocation(gardenLocation.getCityName(), stripAccentCharacters(countryCode)));
	}

	private String stripAccentCharacters(String text) {
		return StringUtils
			.stripAccents(text)
			.replace('Ł', 'L')
			.replace('ł', 'l');
	}

}
