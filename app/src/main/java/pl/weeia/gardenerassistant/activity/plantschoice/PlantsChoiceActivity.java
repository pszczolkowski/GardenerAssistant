package pl.weeia.gardenerassistant.activity.plantschoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.service.PlantsDataService;
import pl.weeia.gardenerassistant.store.SelectedPlantsStore;

public class PlantsChoiceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private List<Plant> plants;
	SelectedPlantsStore selectedPlantsStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plants_choice);

		selectedPlantsStore = new SelectedPlantsStore(this);

		try {
			plants = PlantsDataService.readPlantsData(this);
			preparePlantsListView();
		} catch (IOException e) {
			Toast
				.makeText(getApplicationContext(),"Wystąpił błąd z pobieraniem danych", Toast.LENGTH_SHORT)
				.show();
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int clickedPlantId = plants.get(position).getId();
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.plantsListItemCheckBox);

		if (selectedPlantsStore.isSelected(clickedPlantId)) {
			selectedPlantsStore.removePlantId(clickedPlantId);
			checkbox.setChecked(false);
		} else {
			selectedPlantsStore.addPlantId(clickedPlantId);
			checkbox.setChecked(true);
		}
	}

	private boolean userHasNotSelectedAnyPlants() {
		return selectedPlantsStore.isEmpty();
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
		PlantsListAdapter plantsListAdapter = new PlantsListAdapter(this, R.layout.activity_plants_choice_list_item, plants, selectedPlantsStore);
		plantsListView .setAdapter(plantsListAdapter);
	}

}
