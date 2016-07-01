package pl.weeia.gardenerassistant.activity.actions;

import java.util.Calendar;
import java.util.List;

import pl.weeia.gardenerassistant.model.Action;
import pl.weeia.gardenerassistant.model.Period;
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.model.Repetition;

public class PlantAction {

	private final Action action;
	private final Plant plant;
	private Calendar executionDate;
	private Period period;

	public PlantAction(Action action, Plant plant) {
		this.action = action;
		this.plant = plant;
	}

	public String getName() {
		return action.getName();
	}

	public boolean isCyclic() {
		return action.isCyclic();
	}

	public Repetition getRepeat() {
		return action.getRepeat();
	}

	public List<Period> getPeriods() {
		return action.getPeriods();
	}

	public List<String> getConditions() {
		return action.getConditions();
	}

	public Plant getPlant() {
		return plant;
	}

	public void setExecutionDate(Calendar executionDate) {
		this.executionDate = executionDate;
	}

	public Calendar getExecutionDate() {
		return executionDate;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}
}
