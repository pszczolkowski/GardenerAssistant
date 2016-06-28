package pl.weeia.gardenerassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.weeia.gardenerassistant.activity.plantschoice.PlantsChoiceActivity;

public class ActionsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actions);

		if (userHasNotSelectedAnyPlants()) {
			displayPlantsChoice();
		}
	}

	private boolean userHasNotSelectedAnyPlants() {
		// TODO
		return true;
	}

	private void displayPlantsChoice() {
		Intent intent = new Intent(this, PlantsChoiceActivity.class);
		startActivity(intent);
	}

}
