package no.saiboten;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class TextToSpeechActivity extends Activity implements OnInitListener {

	// TTS object
	private TextToSpeech myTTS;
	// status check code
	private int MY_DATA_CHECK_CODE = 0;

	// create the Activity
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// check for TTS data
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

		speakWords("testing 1-2-3");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void onRestart() {
		super.onRestart();
		speakWords("testing 1-2-3");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	// speak the user text
	private void speakWords(String speech) {

		// speak straight away
		myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
	}

	// act on result of TTS data check
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// the user has the necessary data - create the TTS
				myTTS = new TextToSpeech(this, this);
			} else {
				// no data - install it now
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
	}

	// setup TTS
	public void onInit(int initStatus) {

		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
				myTTS.setLanguage(Locale.US);
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
	}
}
