package no.saiboten;

import no.saiboten.appman.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class AttackTeenagers extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.sound_test);
		mediaPlayer.start(); // no need to call prepare(); create() does that
								// for you

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}
}
