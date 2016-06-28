package pl.weeia.gardenerassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.weeia.gardenerassistant.activity.plantschoice.PlantsChoiceActivity;
import pl.weeia.gardenerassistant.store.SelectedPlantsStore;

public class ActionsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actions);

		if (userHasNotSelectedAnyPlants()) {
			displayPlantsChoice();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_actions_activity, menu);
		return true;
	}

	private boolean userHasNotSelectedAnyPlants() {
		return new SelectedPlantsStore(this).isEmpty();
	}

	private void displayPlantsChoice() {
		Intent intent = new Intent(this, PlantsChoiceActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.selectPlantsButton:
				displayPlantsChoice();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
