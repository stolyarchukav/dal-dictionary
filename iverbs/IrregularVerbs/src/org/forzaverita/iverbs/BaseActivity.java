package org.forzaverita.iverbs;

import java.util.Date;
import java.util.Locale;

import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.preference.AppPreferenceActivity;
import org.forzaverita.iverbs.preference.SelectLangDialog;
import org.forzaverita.iverbs.service.AppService;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements OnInitListener {

	private TextToSpeech tts;
	protected AppService service;
	
	private Date lastPreferencesCheck = new Date();
	
	@Override
	protected void onResume() {
		super.onResume();
		if (service.isPreferencesChanged(lastPreferencesCheck)) {
			lastPreferencesCheck = new Date();
			onCreate(null);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		service = (AppService) getApplicationContext();
		tts = new TextToSpeech(this, this);
		tts.setSpeechRate(service.getSpeechRate());
		tts.setPitch(service.getPitch());
		if (! service.isLanguagePrefered()) {
			startActivity(new Intent(this, SelectLangDialog.class));
		}
	}
	
	protected void onDestroy() {
		if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings :
				startActivity(new Intent(this, AppPreferenceActivity.class));
			break;
			default:
		}
		return true;
	}

	public void onClickHome(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void onClickInfo(View view) {
		startActivity(new Intent(getApplicationContext(), InfoActivity.class));
	}

	public void onClickSearch(View view) {
		onSearchRequested();
	}

	public void onClickMode(View view) {
		switch (view.getId()) {
		case R.id.dashboard_button_table:
			startActivity(new Intent(getApplicationContext(),
					TableActivity.class));
			break;
		case R.id.dashboard_button_learn:
			startActivity(new Intent(getApplicationContext(),
					LearnActivity.class));
			break;
		case R.id.dashboard_button_train:
			startActivity(new Intent(getApplicationContext(),
					TrainActivity.class));
			break;
		/* TODO Future features
		 * case R.id.dashboard_button_scores:
			startActivity(new Intent(getApplicationContext(),
					ScoresActivity.class));
			break;*/
		case R.id.dashboard_button_info:
			startActivity(new Intent(getApplicationContext(),
					InfoActivity.class));
			break;
		default:
			break;
		}
	}

	public void setActivityTitle() {
		TextView textView = (TextView) findViewById(R.id.title_text);
		textView.setText(getTitle());
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(Locale.ENGLISH);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e(Constants.LOG_TAG, "This Language is not supported");
			}
			else {
				setVolumeControlStream(AudioManager.STREAM_MUSIC);
			}
		} else {
			Log.e(Constants.LOG_TAG, "Initilization Failed!");
		}
	}

	protected final void speak(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

}
