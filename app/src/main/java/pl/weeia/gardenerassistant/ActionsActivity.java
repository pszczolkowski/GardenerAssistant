package pl.weeia.gardenerassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.weeia.gardenerassistant.activity.actions.ActionsListAdapter;
import pl.weeia.gardenerassistant.activity.plantschoice.PlantsChoiceActivity;
import pl.weeia.gardenerassistant.model.Action;
import pl.weeia.gardenerassistant.model.Period;
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.service.PlantsDataService;
import pl.weeia.gardenerassistant.store.SelectedPlantsStore;

public class ActionsActivity extends AppCompatActivity {

	private static final int NEAR_TIME_PERIOD_DAYS = 7;

	private List<Action> actions = new ArrayList<>();
	private ActionsListAdapter actionsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actions);

		if (userHasNotSelectedAnyPlants()) {
			displayPlantsChoice();
			return;
		}

		ListView actionsListView = (ListView) findViewById(R.id.actionsListView);
		actionsListAdapter = new ActionsListAdapter(this, R.layout.activity_actions_list_item, actions);
		actionsListView.setAdapter(actionsListAdapter);
		displayActionsToExecute();
	}

	private void displayPlantsChoice() {
		Intent intent = new Intent(this, PlantsChoiceActivity.class);
		startActivity(intent);
	}

	private void displayActionsToExecute() {
		try {
			List<Plant> plants = PlantsDataService.readPlantsData(this);
			List<Action> actions = fetchAllActionsFromPlants(plants);
			List<Action> actionsToExecute = filterActionsToExecute(actions);
			sortActionsByDueDate(actionsToExecute);
			displayActions(actionsToExecute);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Action> fetchAllActionsFromPlants(List<Plant> plants) {
		List<Action> allActions = new ArrayList<>();
		for (Plant plant : plants) {
			allActions.addAll(plant.getActions());
		}

		return allActions;
	}

	private List<Action> filterActionsToExecute(List<Action> actions) {
		List<Action> actionsToExecute = new ArrayList<>();
		for (Action action : actions) {
			if (actionExecutionIsInNearTimePeriod(action) && actionHasNotBeenExecutedToday(action)) {
				actionsToExecute.add(action);
			}
		}

		return actionsToExecute;
	}

	private boolean actionExecutionIsInNearTimePeriod(Action action) {
		Calendar endOfNearTimePeriod = Calendar.getInstance();
		endOfNearTimePeriod.add(Calendar.DAY_OF_YEAR, NEAR_TIME_PERIOD_DAYS);

		for (Period period : action.getPeriod()) {
			if (period.isBetween(now(), endOfNearTimePeriod)) {
				return true;
			}
		}

		return false;
	}

	private Calendar now() {
		return Calendar.getInstance();
	}

	private boolean actionHasNotBeenExecutedToday(Action action) {
		// TODO
		return true;
	}

	private void sortActionsByDueDate(List<Action> actionsToExecute) {
		Collections.sort(actionsToExecute, new Comparator<Action>() {
			@Override
			public int compare(Action first, Action second) {
				if (findTheEarliestPeriod(first).startsBefore(findTheEarliestPeriod(second))) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}

	private Period findTheEarliestPeriod(Action action) {
		Period theEarliestPeriod = action.getPeriod().get(0);

		for (Period period : action.getPeriod()) {
			if (period.startsBefore(theEarliestPeriod)) {
				theEarliestPeriod = period;
			}
		}

		return theEarliestPeriod;
	}

	private void displayActions(List<Action> actionsToExecute) {
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
