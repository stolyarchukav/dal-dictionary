package org.forzaverita.iverbs;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
		startActivity(new Intent(getApplicationContext(), SearchActivity.class));
	}	

	public void onClickMode(View view) {
		switch (view.getId()) {
		case R.id.dashboard_button_table:
			startActivity(new Intent(getApplicationContext(), TableActivity.class));
			break;
		case R.id.dashboard_button_learn:
			startActivity(new Intent(getApplicationContext(), LearnActivity.class));
			break;
		case R.id.dashboard_button_train:
			startActivity(new Intent(getApplicationContext(), TrainActivity.class));
			break;
		case R.id.dashboard_button_scores:
			startActivity(new Intent(getApplicationContext(), ScoresActivity.class));
			break;
		default:
			break;
		}
	}

	public void setActivityTitle() {
		TextView textView = (TextView) findViewById(R.id.title_text);
		textView.setText(getTitle());
	}

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public void trace(String msg) {
		Log.d("Demo", msg);
		toast(msg);
	}

}
