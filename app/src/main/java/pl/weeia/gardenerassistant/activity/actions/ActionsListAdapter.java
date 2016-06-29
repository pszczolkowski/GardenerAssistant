package pl.weeia.gardenerassistant.activity.actions;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.model.Action;

public class ActionsListAdapter extends ArrayAdapter<Action> {

	public ActionsListAdapter(Context context, int resource, List<Action> actions) {
		super(context, resource, actions);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = inflateLayoutForItem(view, parent);
		Action action = getItem(position);
		customizeViewForTheAction(view, action);

		return view;
	}

	private View inflateLayoutForItem(View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater layoutInflater;
			layoutInflater = LayoutInflater.from(getContext());
			view = layoutInflater.inflate(R.layout.activity_actions_list_item, parent, false);
		}
		return view;
	}

	private void customizeViewForTheAction(View view, Action action) {
		if (action != null) {
			TextView textView = (TextView) view.findViewById(R.id.actionsListItemName);
			textView.setText(action.getName());
		}
	}

}
