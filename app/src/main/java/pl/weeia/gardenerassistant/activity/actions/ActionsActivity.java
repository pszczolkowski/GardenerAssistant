package pl.weeia.gardenerassistant.activity.actions;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.activity.plantschoice.PlantsChoiceActivity;
import pl.weeia.gardenerassistant.model.Action;
import pl.weeia.gardenerassistant.model.Period;
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.model.Repetition;
import pl.weeia.gardenerassistant.service.PlantsDataService;
import pl.weeia.gardenerassistant.store.SelectedPlantsStore;

public class ActionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private static final int NEAR_TIME_PERIOD_DAYS = 7;

	private List<PlantAction> actions = new ArrayList<>();
	private ActionsListAdapter actionsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actions);

		if (userHasNotSelectedAnyPlants()) {
			displayPlantsChoice();
			return;
		}

		prepareLayout();
		displayActionsToExecute();
	}

	private void displayPlantsChoice() {
		Intent intent = new Intent(this, PlantsChoiceActivity.class);
		startActivity(intent);
	}

	private void prepareLayout() {
		ListView actionsListView = (ListView) findViewById(R.id.actionsListView);
		actionsListView.setOnItemClickListener(this);
		actionsListAdapter = new ActionsListAdapter(this, actions);
		actionsListView.setAdapter(actionsListAdapter);
	}

	private void displayActionsToExecute() {
		try {
			List<Plant> plants = PlantsDataService.readPlantsData(this);
			List<PlantAction> actions = fetchAllActionsFromPlants(plants);
			List<PlantAction> actionsToExecute = fetchActionsToExecute(actions);
			sortActionsByDueDate(actionsToExecute);
			displayActions(actionsToExecute);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<PlantAction> fetchAllActionsFromPlants(List<Plant> plants) {
		List<PlantAction> allActions = new ArrayList<>();
		for (Plant plant : plants) {
			for (Action action : plant.getActions()) {
				allActions.add(new PlantAction(action, plant));
			}
		}

		return allActions;
	}

	private List<PlantAction> fetchActionsToExecute(List<PlantAction> actions) {
		List<PlantAction> actionsToExecute = new ArrayList<>();
		for (PlantAction action : actions) {
			if (actionExecutionIsInNearTimePeriod(action)) {
				if (action.isCyclic()) {
					if (action.getRepeat() == Repetition.DAILY) {
						action.setExecutionDate(now());
					}
				}

				actionsToExecute.add(action);
			}
		}

		return actionsToExecute;
	}

	private boolean actionExecutionIsInNearTimePeriod(PlantAction action) {
		Calendar endOfNearTimePeriod = Calendar.getInstance();
		endOfNearTimePeriod.add(Calendar.DAY_OF_YEAR, NEAR_TIME_PERIOD_DAYS);

		for (Period period : action.getPeriods()) {
			if (period.isBetween(now(), endOfNearTimePeriod)) {
				return true;
			}
		}

		return false;
	}

	private Calendar now() {
		return Calendar.getInstance();
	}

	private boolean actionHasNotBeenExecutedToday(PlantAction action) {
		// TODO
		return true;
	}

	private void sortActionsByDueDate(List<PlantAction> actionsToExecute) {
		Collections.sort(actionsToExecute, new Comparator<PlantAction>() {
			@Override
			public int compare(PlantAction first, PlantAction second) {
				if (findTheEarliestPeriod(first).startsBefore(findTheEarliestPeriod(second))) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}

	private Period findTheEarliestPeriod(PlantAction action) {
		Period theEarliestPeriod = action.getPeriods().get(0);

		for (Period period : action.getPeriods()) {
			if (period.startsBefore(theEarliestPeriod)) {
				theEarliestPeriod = period;
			}
		}

		return theEarliestPeriod;
	}

	private void displayActions(List<PlantAction> actionsToExecute) {
		actionsListAdapter.addAll(actionsToExecute);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.selectPlantsButton:
				displayPlantsChoice();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
