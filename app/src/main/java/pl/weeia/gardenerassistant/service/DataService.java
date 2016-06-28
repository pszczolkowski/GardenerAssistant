package pl.weeia.gardenerassistant.service;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.model.Plant;

public class DataService {

	public static List<Plant> getPlants(Context context) throws IOException {
		InputStream inputStream = context.getResources().openRawResource(R.raw.plants);
		ObjectMapper objectMapper = new ObjectMapper();

		List<Plant> plants = objectMapper.readValue(inputStream, new TypeReference<List<Plant>>(){});
		Collections.sort(plants, new Comparator<Plant>() {
			@Override
			public int compare(Plant first, Plant second) {
				return first.getName().compareToIgnoreCase(second.getName());
			}
		});

		return plants;
	}

}
