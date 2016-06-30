package pl.weeia.gardenerassistant.repository.action;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.databind.deser.std.DateDeserializers.CalendarDeserializer;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;

import java.util.Calendar;

public class ExecutedAction {

	private int plantId;
	private String actionName;
	@JsonSerialize(using = CalendarSerializer.class)
	@JsonDeserialize(using = CalendarDeserializer.class)
	private Calendar executionDate;

	protected ExecutedAction() {}

	public ExecutedAction(int plantId, String actionName, Calendar executionDate) {
		this.plantId = plantId;
		this.actionName = actionName;
		this.executionDate = executionDate;
	}

	public int getPlantId() {
		return plantId;
	}

	public String getActionName() {
		return actionName;
	}

	public Calendar getExecutionDate() {
		return executionDate;
	}

}
