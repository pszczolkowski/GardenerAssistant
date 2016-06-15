package pl.weeia.gardenerassistant.activity.plantschoice;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.service.DataService;

public class PlantsChoiceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private static final String SHARED_PREFERENCES_NAME = "preferences";
	private static final String SHARED_PREFERENCES_SELECTED_PLANTS = "selectedPlantsIds";

	private List<Plant> plants;
	private Set<Integer> selectedPlantsIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plants_choice);

		selectedPlantsIds = loadSelectedPlantsIds();

		try {
			plants = DataService.getPlants(this);
			preparePlantsListView();
		} catch (IOException e) {
			Toast
				.makeText(getApplicationContext(),"Wystąpił błąd z pobieraniem danych", Toast.LENGTH_SHORT)
				.show();
			e.printStackTrace();
		}
	}

	private Set<Integer> loadSelectedPlantsIds() {
		SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		Set<String> stringSet = sharedPreferences.getStringSet(SHARED_PREFERENCES_SELECTED_PLANTS, new HashSet<String>());

		return mapToIntegersSet(stringSet);
	}

	private Set<Integer> mapToIntegersSet(Set<String> stringSet) {
		Set<Integer> integerSet = new HashSet<>();
		for (String string : stringSet) {
			integerSet.add(Integer.parseInt(string));
		}

		return integerSet;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int clickedPlantId = plants.get(position).getId();
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.plantsListItemCheckBox);

		if (selectedPlantsIds.contains(clickedPlantId)) {
			selectedPlantsIds.remove(clickedPlantId);
			checkbox.setChecked(false);
		} else {
			selectedPlantsIds.add(clickedPlantId);
			checkbox.setChecked(true);
		}

		saveSelectedPlantsIds();
	}

	private void saveSelectedPlantsIds() {
		SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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

	private boolean userHasNotSelectedAnyPlants() {
		return selectedPlantsIds.isEmpty();
	}

	private void onReturnRequest() {
		if (userHasNotSelectedAnyPlants()) {
			Toast
				.makeText(getApplicationContext(),"Musisz wybrać przynajmniej jedną roślinę", Toast.LENGTH_SHORT)
				.show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onBackPressed() {
		onReturnRequest();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onReturnRequest();
		return true;
	}

	private void preparePlantsListView() {
		ListView plantsListView = (ListView) findViewById(R.id.plantsListView);
		plantsListView.setOnItemClickListener(this);
		PlantsListAdapter plantsListAdapter = new PlantsListAdapter(this, R.layout.activity_plants_choice_list_item, plants, selectedPlantsIds);
		plantsListView .setAdapter(plantsListAdapter);
	}

}
