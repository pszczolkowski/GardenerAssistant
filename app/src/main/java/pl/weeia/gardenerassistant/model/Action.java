package pl.weeia.gardenerassistant.model;

import java.util.ArrayList;
import java.util.List;

public class Action {

	private String name;
	private boolean cyclic = false;
	private Repetition repeat;
	private List<Period> periods = new ArrayList<>();
	private List<String> conditions = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCyclic() {
		return cyclic;
	}

	public void setCyclic(boolean cyclic) {
		this.cyclic = cyclic;
	}

	public Repetition getRepeat() {
		return repeat;
	}

	public void setRepeat(Repetition repeat) {
		this.repeat = repeat;
	}

	public List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = new ArrayList<>(periods);
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = new ArrayList<>(conditions);
	}
}
