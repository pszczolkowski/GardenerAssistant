package pl.weeia.gardenerassistant.activity.plantschoice;

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
import pl.weeia.gardenerassistant.model.Plant;
import pl.weeia.gardenerassistant.store.plant.SelectedPlantsStore;

public class PlantsListAdapter extends ArrayAdapter<Plant> {

	private SelectedPlantsStore selectedPlantsStore;

	public PlantsListAdapter(Context context, int resource, List<Plant> plants, SelectedPlantsStore selectedPlantsStore) {
		super(context, resource, plants);
		this.selectedPlantsStore = selectedPlantsStore;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = inflateLayoutForItem(view, parent);
		Plant plant = getItem(position);
		customizeViewForThePlant(view, plant);

		return view;
	}

	private View inflateLayoutForItem(View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater layoutInflater;
			layoutInflater = LayoutInflater.from(getContext());
			view = layoutInflater.inflate(R.layout.activity_plants_choice_list_item, parent, false);
		}
		return view;
	}

	private void customizeViewForThePlant(View view, Plant plant) {
		if (plant != null) {
			ImageView image = (ImageView) view.findViewById(R.id.plantsListItemImage);
			int drawableIdentifier = getContext().getResources().getIdentifier(plant.getIcon(), "drawable", getContext().getPackageName());
			image.setImageResource(drawableIdentifier);

			TextView textView = (TextView) view.findViewById(R.id.plantsListItemName);
			textView.setText(plant.getName());

			AppCompatCheckBox checkbox = (AppCompatCheckBox) view.findViewById(R.id.plantsListItemCheckBox);
			if (selectedPlantsStore.isSelected(plant.getId())) {
				checkbox.setChecked(true);
			} else {
				checkbox.setChecked(false);
			}
		}
	}

}
