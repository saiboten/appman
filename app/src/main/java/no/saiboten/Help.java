package no.saiboten;

import no.saiboten.appman.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends Activity {

	Help helpClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		helpClass = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		Button speakButton = (Button) findViewById(R.id.backFromHelp);

		speakButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(helpClass, MainActivity.class);
				startActivity(intent);
			}

		});

	}
}
