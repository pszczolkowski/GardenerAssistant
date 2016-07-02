package pl.weeia.gardenerassistant.activity.actions;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.activity.locationchoice.LocationChoiceActivity;
import pl.weeia.gardenerassistant.activity.plantschoice.PlantsChoiceActivity;
import pl.weeia.gardenerassistant.activity.welcome.WelcomeActivity;
import pl.weeia.gardenerassistant.model.Action;
import pl.weeia.gardenerassistant.model.Period;
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.repository.action.ExecutedActionsRepository;
import pl.weeia.gardenerassistant.repository.plant.SelectedPlantsRepository;
import pl.weeia.gardenerassistant.service.PlantsDataService;
import pl.weeia.gardenerassistant.service.condition.ConditionChecker;
import pl.weeia.gardenerassistant.service.weather.WeatherService;
import pl.weeia.gardenerassistant.util.DateUtil;

public class ActionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private static final int NEAR_TIME_PERIOD_DAYS = 7;

	private ActionsListAdapter actionsListAdapter;
	private ExecutedActionsRepository executedActionsRepository;
	private SelectedPlantsRepository selectedPlantsStore;
	private ConditionChecker conditionChecker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actions);

		executedActionsRepository = new ExecutedActionsRepository(this);
		selectedPlantsStore = new SelectedPlantsRepository(this);
		conditionChecker = new ConditionChecker(this);

		if (userHasNotSelectedAnyPlants()) {
			displayWelcomeView();
			return;
		}

		new WeatherService(this).refresh();
		prepareLayout();
		displayActionsToExecute();
	}

	private void displayWelcomeView() {
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();

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
		actionsListAdapter = new ActionsListAdapter(this);
		actionsListView.setAdapter(actionsListAdapter);
	}

	private void displayActionsToExecute() {
		try {
			List<Plant> plants = PlantsDataService.readPlantsData(this);
			List<PlantAction> actions = fetchAllActionsFromSelectedPlants(plants);
			List<PlantAction> actionsToExecute = fetchActionsToExecute(actions);
			sortActionsByDueDate(actionsToExecute);
			displayActions(actionsToExecute);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<PlantAction> fetchAllActionsFromSelectedPlants(List<Plant> plants) {
		List<PlantAction> allActions = new ArrayList<>();
		for (Plant plant : plants) {
			if (plantIsNotSelectedByUser(plant)) {
				continue;
			}

			for (Action action : plant.getActions()) {
				allActions.add(new PlantAction(action, plant));
			}
		}

		return allActions;
	}

	private boolean plantIsNotSelectedByUser(Plant plant) {
		return !selectedPlantsStore.isSelected(plant.getId());
	}

	private List<PlantAction> fetchActionsToExecute(List<PlantAction> actions) {
		List<PlantAction> actionsToExecute = new ArrayList<>();
		for (PlantAction action : actions) {
			Period period = findNearTimePeriodOf(action);
			if (period == null) {
				continue;
			}

			if (conditionsAreNotMet(action)) {
				continue;
			}

			if (action.isCyclic()) {
				switch (action.getRepeat()) {
					case DAILY:
						if (actionHasNotBeenExecutedToday(action)) {
							action.setExecutionDate(today());
						} else {
							action.setExecutionDate(tomorrow());
						}

						actionsToExecute.add(action);
						break;
					default:
						throw new IllegalStateException("Unsupported repetition <" + action.getRepeat() + ">");
				}
			} else if (actionHasNotBeenExecutedInPeriod(action, period)) {
				if (action.getConditions().isEmpty()) {
					action.setPeriod(period);
				} else {
					action.setExecutionDate(today());
				}

				actionsToExecute.add(action);
			}
		}

		return actionsToExecute;
	}

	private Period findNearTimePeriodOf(PlantAction action) {
		// if two periods are contained in near time, then only one of them
		// is returned. It's not guaranteed that it will be the earlier one

		Calendar endOfNearTimePeriod = Calendar.getInstance();
		endOfNearTimePeriod.add(Calendar.DAY_OF_YEAR, NEAR_TIME_PERIOD_DAYS);

		for (Period period : action.getPeriods()) {
			if (period.isBetween(today(), endOfNearTimePeriod)) {
				return period;
			}
		}

		return null;
	}

	private boolean conditionsAreNotMet(PlantAction action) {
		for (String condition : action.getConditions()) {
			if (!conditionChecker.check(condition)) {
				return true;
			}
		}

		return false;
	}

	private boolean actionHasNotBeenExecutedToday(PlantAction action) {
		return executedActionsRepository.findByPlantIdAndNameAndDate(action.getPlant().getId(), action.getName(), today()) == null;
	}

	private boolean actionHasNotBeenExecutedInPeriod(PlantAction action, Period period) {
		return executedActionsRepository.findByPlantIdAndNameAndDateIn(action.getPlant().getId(), action.getName(), period) == null;
	}

	private Calendar today() {
		return Calendar.getInstance();
	}

	private Calendar tomorrow() {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);
		return tomorrow;
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
		if (actionsToExecute.isEmpty()) {
			findViewById(R.id.noActionsLabel).setVisibility(View.VISIBLE);
			findViewById(R.id.actionsListView).setVisibility(View.GONE);
		} else {
			findViewById(R.id.noActionsLabel).setVisibility(View.GONE);
			findViewById(R.id.actionsListView).setVisibility(View.VISIBLE);
		}

		actionsListAdapter.setAll(actionsToExecute);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_actions_activity, menu);
		return true;
	}

	private boolean userHasNotSelectedAnyPlants() {
		return new SelectedPlantsRepository(this).isEmpty();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.selectPlantsButton:
				displayPlantsChoice();
				return true;
			case R.id.selectLocationButton:
				displayLocationChoice();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void displayLocationChoice() {
		Intent intent = new Intent(this, LocationChoiceActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final PlantAction action = (PlantAction) actionsListAdapter.getItem(position);

		if (actionnCannotBeExecutedToday(action)) {
			shortToast("Dzisiaj nie można zakończyć tej czynności");
		} else {
			askUserToConfirmExecutingTheAction(action);
		}
	}

	private void askUserToConfirmExecutingTheAction(final PlantAction action) {
		new AlertDialog.Builder(this)
			.setTitle(action.getName())
			.setMessage("Czy na pewno chcesz oznaczyć tę czynność jako wykonaną?")
			.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						CheckBox checkBox = (CheckBox) findViewById(R.id.actionsListItemCheckBox);
						checkBox.setChecked(true);

						executeAction(action);
					} catch (IOException e) {
						e.printStackTrace();
						shortToast("Wystąpił błąd");
					}
				}
			})
			.setNegativeButton("Nie", null)
			.show();
	}

	private boolean actionnCannotBeExecutedToday(PlantAction action) {
		if (action.getExecutionDate() != null) {
			if (!DateUtil.isToday(action.getExecutionDate())) {
				return true;
			}
		} else {
			Period nearTimePeriod = findNearTimePeriodOf(action);
			if (nearTimePeriod != null && !nearTimePeriod.contains(today())) {
				return true;
			}
		}

		return false;
	}

	private void shortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void executeAction(PlantAction action) throws IOException {
		executedActionsRepository.addExecutedAction(action);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						displayActionsToExecute();
					}
				});
			}
		}, 500);
	}

}
