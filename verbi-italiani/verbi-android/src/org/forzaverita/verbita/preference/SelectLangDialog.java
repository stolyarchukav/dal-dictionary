package org.forzaverita.verbita.preference;

import org.forzaverita.verbita.R;
import org.forzaverita.verbita.data.Lang;
import org.forzaverita.verbita.service.AppService;

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
