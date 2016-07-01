package pl.weeia.gardenerassistant.activity.welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.weeia.gardenerassistant.R;
import pl.weeia.gardenerassistant.activity.plantschoice.PlantsChoiceActivity;

public class WelcomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		Button confirmButton = (Button) findViewById(R.id.welcomeActivityConfirmButton);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.this, PlantsChoiceActivity.class);
				startActivity(intent);
			}
		});
	}

}
