package pl.weeia.gardenerassistant.model;

import java.util.ArrayList;
import java.util.List;

public class Plant {

	private int id;
	private String name;
	private String icon;
	private boolean perennial;
	private List<Action> actions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isPerennial() {
		return perennial;
	}

	public void setPerennial(boolean perennial) {
		this.perennial = perennial;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = new ArrayList<>(actions);
	}
}
