package pl.weeia.gardenerassistant.activity.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.util.DateUtil;

public class ActionsListAdapter extends BaseAdapter {

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
	private final Context context;

	private List<PlantAction> todayActions = new ArrayList<>();
	private List<PlantAction> tomorrowActions = new ArrayList<>();
	private List<PlantAction> nearFutureActions = new ArrayList<>();

	public ActionsListAdapter(Context context) {
		this.context = context;
	}

	public void setAll(List<PlantAction> actions) {
		todayActions.clear();
		tomorrowActions.clear();
		nearFutureActions.clear();

		for (PlantAction action : actions) {
			if (action.getExecutionDate() == null) {
				nearFutureActions.add(action);
			} else if (DateUtil.isToday(action.getExecutionDate())) {
				todayActions.add(action);
			} else if (DateUtil.isTomorrow(action.getExecutionDate())) {
				tomorrowActions.add(action);
			} else {
				nearFutureActions.add(action);
			}
		}

		Collections.sort(todayActions, plantActionsComparator);
		Collections.sort(tomorrowActions, plantActionsComparator);
		Collections.sort(nearFutureActions, plantActionsComparator);

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		int sumOfElements = 0;
		sumOfElements += todayActions.isEmpty() ? 0 : todayActions.size() + 1;
		sumOfElements += tomorrowActions.isEmpty() ? 0 : tomorrowActions.size() + 1;
		sumOfElements += nearFutureActions.isEmpty() ? 0 : nearFutureActions.size() + 1;

		return sumOfElements;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		if (!todayActions.isEmpty()) {
			if (position <= todayActions.size()) {
				return position == 0 ? "DZISIAJ" : todayActions.get(position - 1);
			} else {
				position -= todayActions.size() + 1;
			}
		}

		if (!tomorrowActions.isEmpty()) {
			if (position <= tomorrowActions.size()) {
				return position == 0 ? "JUTRO" : tomorrowActions.get(position - 1);
			} else {
				position -= tomorrowActions.size() + 1;
			}
		}

		return position == 0 ? "W NAJBLIŻSZYM CZASIE" : nearFutureActions.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItem(position) instanceof PlantAction;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Object item = getItem(position);

		if (item instanceof String) {
			return inflateLayoutForHeader(position, view, parent);
		} else {
			return inflateLayoutForItem((PlantAction)item, view, parent);
		}
	}

	private View inflateLayoutForHeader(int position, View view, ViewGroup parent) {
		LayoutInflater layoutInflater;
		layoutInflater = LayoutInflater.from(context);
		view = layoutInflater.inflate(R.layout.activity_actions_list_header, parent, false);

		TextView headerTextView = (TextView) view.findViewById(R.id.actionsListHeaderText);
		headerTextView.setText((String)getItem(position));

		if (position > 0) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 50, 0, 0);
			headerTextView.setLayoutParams(layoutParams);
		}

		return view;
	}

	private View inflateLayoutForItem(PlantAction action, View view, ViewGroup parent) {
		LayoutInflater layoutInflater;
		layoutInflater = LayoutInflater.from(context);
		view = layoutInflater.inflate(R.layout.activity_actions_list_item, parent, false);

		TextView textView = (TextView) view.findViewById(R.id.actionsListItemName);
		textView.setText(action.getName());

		TextView executionDateView = (TextView) view.findViewById(R.id.actionsListItemExecutionDate);
		executionDateView.setText(dateToString(action));

		if (!action.getConditions().isEmpty()) {
			for (String condition : action.getConditions()) {
				if (condition.matches("temperature\\s*<=?\\s*[0-9]+(\\.[0-9]+)?")) {
					TextView conditionsTextView = (TextView) view.findViewById(R.id.actionsListItemConditions);
					conditionsTextView.setVisibility(View.VISIBLE);
					conditionsTextView.setText("Kiedy temperatura poniżej " + condition.substring(12) + " ℃" );
				}
			}

		}

		return view;
	}

	private String dateToString(PlantAction action) {
		if (action.getExecutionDate() != null) {
			if (DateUtil.isToday(action.getExecutionDate())) {
				return "Dzisiaj";
			} else if (DateUtil.isTomorrow(action.getExecutionDate())) {
				return "Jutro";
			} else {
				return simpleDateFormat.format(action.getExecutionDate().getTime());
			}
		} else {
			String result = simpleDateFormat.format(action.getPeriod().getStart().getTime());
			result += " - " + simpleDateFormat.format(action.getPeriod().getEnd().getTime());

			return result;
		}
	}

	private static Comparator<PlantAction> plantActionsComparator = new Comparator<PlantAction>() {
		@Override
		public int compare(PlantAction first, PlantAction second) {
			if (first.getExecutionDate() != null && second.getExecutionDate() != null) {
				if (DateUtil.isBefore(first.getExecutionDate(), second.getExecutionDate())) {
					return -1;
				} else if (DateUtil.areEqual(first.getExecutionDate(), second.getExecutionDate())) {
					return 0;
				} else {
					return 1;
				}
			} else if (second.getExecutionDate() == null) {
				return -1;
			} else {
				return 1;
			}
		}
	};
}
