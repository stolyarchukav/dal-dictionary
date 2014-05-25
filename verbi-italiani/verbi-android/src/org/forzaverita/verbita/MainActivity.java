package org.forzaverita.verbita;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
