package pl.weeia.gardenerassistant.repository.action;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.weeia.gardenerassistant.activity.actions.PlantAction;
import pl.weeia.gardenerassistant.model.Period;
import pl.weeia.gardenerassistant.util.DateUtil;

public class ExecutedActionsRepository {

	private static final String FILE_NAME = "executed_actions_store";

	private static List<ExecutedAction> executedActions;

	private Context context;

	public ExecutedActionsRepository(Context context) {
		this.context = context;
	}

	private void saveExecutedActions() throws IOException {
		File file = new File(context.getFilesDir(), FILE_NAME);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(file, executedActions);
	}

	public void addExecutedAction(PlantAction action) throws IOException {
		executedActions.add(new ExecutedAction(action.getPlant().getId(), action.getName(), Calendar.getInstance()));
		saveExecutedActions();
	}

	public ExecutedAction findByPlantIdAndNameAndDate(int plantId, String actionName, Calendar date) {
		readExecutedActionsFromStoreIfNecessary();

		for (ExecutedAction executedAction : executedActions) {
			if (executedAction.getPlantId() == plantId &&
				executedAction.getActionName().equals(actionName) &&
				DateUtil.areEqual(executedAction.getExecutionDate(), date)) {
				return executedAction;
			}
		}

		return null;
	}

	private void readExecutedActionsFromStoreIfNecessary() {
		if (executedActions == null) {
			File file = new File(context.getFilesDir(), FILE_NAME);
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				executedActions = objectMapper.readValue(file, new TypeReference<List<ExecutedAction>>(){});
			} catch (IOException e) {
				executedActions = new ArrayList<>();
			}
		}
	}

	public ExecutedAction findByPlantIdAndNameAndDateIn(int plantId, String actionName, Period period) {
		readExecutedActionsFromStoreIfNecessary();

		for (ExecutedAction executedAction : executedActions) {
			if (executedAction.getPlantId() == plantId &&
				executedAction.getActionName().equals(actionName) &&
				period.contains(executedAction.getExecutionDate())) {
				return executedAction;
			}
		}

		return null;
	}
}
