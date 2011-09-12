package org.forzaverita.daldic;

import org.forzaverita.daldic.preferences.AppPreferenceActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

public abstract class AbstractActivity extends Activity {
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			startActivity(new Intent(this, AppPreferenceActivity.class));
		}
		return true;
	}

}
