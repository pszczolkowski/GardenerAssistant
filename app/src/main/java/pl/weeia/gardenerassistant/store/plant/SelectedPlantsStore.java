package pl.weeia.gardenerassistant.store.plant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SelectedPlantsStore {

	private static final String SHARED_PREFERENCES_NAME = "preferences";
	private static final String SHARED_PREFERENCES_SELECTED_PLANTS = "selectedPlantsIds";

	private static Set<Integer> selectedPlantsIds;

	private Context context;

	public SelectedPlantsStore(Context context) {
		this.context = context;

		if (selectedPlantsIds == null) {
			loadSelectedPlantsIds();
		}
	}

	public boolean isSelected(int plantId) {
		return selectedPlantsIds.contains(plantId);
	}

	public boolean isEmpty() {
		return selectedPlantsIds.isEmpty();
	}

	private void loadSelectedPlantsIds() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		Set<String> stringSet = sharedPreferences.getStringSet(SHARED_PREFERENCES_SELECTED_PLANTS, new HashSet<String>());

		selectedPlantsIds = mapToIntegersSet(stringSet);
	}

	private Set<Integer> mapToIntegersSet(Set<String> stringSet) {
		Set<Integer> integerSet = new HashSet<>();
		for (String string : stringSet) {
			integerSet.add(Integer.parseInt(string));
		}

		return integerSet;
	}

	public void addPlantId(int id) {
		selectedPlantsIds.add(id);
		saveSelectedPlantsIds();
	}

	public void removePlantId(int id) {
		selectedPlantsIds.remove(id);
		saveSelectedPlantsIds();
	}

	private void saveSelectedPlantsIds() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		sharedPreferences
			.edit()
			.putStringSet(SHARED_PREFERENCES_SELECTED_PLANTS, mapToStringSet(selectedPlantsIds))
			.apply();
	}

	private Set<String> mapToStringSet(Set<Integer> integerSet) {
		Set<String> stringSet = new HashSet<>();
		for (Integer integer : integerSet) {
			stringSet.add(String.valueOf(integer));
		}

		return stringSet;
	}

}
