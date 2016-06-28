package pl.weeia.gardenerassistant.model;

import java.util.ArrayList;
import java.util.List;

public class Action {

	private String name;
	private boolean cyclic;
	private String repeat;
	private List<Period> period;
	private List<String> conditions;

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

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public List<Period> getPeriod() {
		return period;
	}

	public void setPeriod(List<Period> period) {
		this.period = new ArrayList<>(period);
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = new ArrayList<>(conditions);
	}
}
