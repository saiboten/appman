package no.saiboten;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import no.saiboten.appman.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnInitListener {

	private static final int RECOGNIZER_RESULT = 1234;

	public final static String EXTRA_MESSAGE = "no.saiboten.appman.GOOGLE";

	private boolean flashOn = false;

	private Camera cam;

	private int MY_DATA_CHECK_CODE = 0;

	private TextToSpeech myTTS;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speech_to_text_layout);

		Button startSpeech = (Button) findViewById(R.id.getSpeechButton);
		startSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text");
				startActivityForResult(intent, RECOGNIZER_RESULT);
			}

		});

		Button speakButton = (Button) findViewById(R.id.speak);
		speakButton.setOnClickListener(this);

		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				myTTS = new TextToSpeech(this, this);
			} else {
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}

		if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			TextView speechText = (TextView) findViewById(R.id.speechText);
			speechText.setText(matches.get(0).toString());

			for (String match : matches) {
				Log.d("MainActivity", "Match: " + match);
			}

			if (matches.get(0).toString().contains("attack teenagers")) {
				MediaPlayer mediaPlayer = MediaPlayer.create(
						getApplicationContext(), R.raw.sound_test);
				mediaPlayer.start(); // no need to call prepare(); create() does
										// that
										// for you
			} else if (matches.get(0).toString().startsWith("google")) {
				String query = matches.get(0).toString()
						.substring(7, matches.get(0).toString().length());

				Uri webpage = Uri.parse("http://www.google.no/search?q="
						+ query);
				Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
				startActivity(webIntent);
			} else if (matches.get(0).toString().contains("theme")) {
				MediaPlayer mediaPlayer = MediaPlayer.create(
						getApplicationContext(), R.raw.appman_theme);
				mediaPlayer.start(); // no need to call prepare(); create() does
										// that
										// for you
			} else if (listContains(matches, "flashlight")
					|| listContains(matches, "light ray")) {
				if (flashOn) {
					cam.stopPreview();
					cam.release();

				} else {
					cam = Camera.open();
					Parameters p = cam.getParameters();
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					cam.setParameters(p);
					cam.startPreview();
				}
				flashOn = !flashOn;
			} else if (listContains(matches, "clock")) {
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("k m");
				speakWords("The time is " + sdf.format(c.getTime()));
			} else if (listContains(matches, "birthday")) {
				speakWords("Happy birthday to you, happy birthday to you, happy birthday to Bursdagsbarn, happy birthday to you");
			} else if (listContains(matches, "backup")) {
				speakWords("Sending backup!");
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage("99887766", null, "NEED BACKUP!", null,
						null);
			} else if (listContains(matches, "help")) {
				Intent intent = new Intent(this, Help.class);
				startActivity(intent);
			} else if (matches.get(0).startsWith("backwards")) {
				speakWords(new StringBuilder(matches.get(0).substring(9,
						matches.get(0).length())).reverse().toString());
			} else if (listContains(matches, "fox")) {
				if (Math.random() < 0.5) {
					MediaPlayer mediaPlayer = MediaPlayer.create(
							getApplicationContext(), R.raw.thefox1);
					mediaPlayer.start();
				} else {
					MediaPlayer mediaPlayer = MediaPlayer.create(
							getApplicationContext(), R.raw.thefox2);
					mediaPlayer.start();
				}

			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean listContains(List<String> haystack, String needle) {

		boolean contains = false;
		for (String str : haystack) {
			if (str.contains(needle)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	@Override
	public void onClick(View v) {
		EditText enteredText = (EditText) findViewById(R.id.enter);
		String words = enteredText.getText().toString();
		speakWords(words);
	}

	private void speakWords(String speech) {
		myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int initStatus) {
		if (initStatus == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.US);
		}
	}
}
