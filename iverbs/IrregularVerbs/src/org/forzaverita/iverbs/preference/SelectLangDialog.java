package org.forzaverita.iverbs.preference;

import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.data.Lang;
import org.forzaverita.iverbs.service.AppService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class SelectLangDialog extends Activity {

	private AppService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_lang);
		service = (AppService) this.getApplicationContext();
		RadioGroup group = (RadioGroup) findViewById(R.id.selectLangGroup);
		group.check(service.getLanguage().getId());
	}
	
	public void selectLang(View view) {
		RadioGroup group = (RadioGroup) findViewById(R.id.selectLangGroup);
		int selectedId = group.getCheckedRadioButtonId();
		Lang lang = Lang.valueOf(selectedId);
		service.setLanguage(lang);
		service.preferencesChanged();
		finish();
	}
	
}
