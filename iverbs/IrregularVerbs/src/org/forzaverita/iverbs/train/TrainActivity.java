package org.forzaverita.iverbs.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

import org.forzaverita.iverbs.BaseActivity;
import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.ScoresActivity;
import org.forzaverita.iverbs.fragment.MapFragmentsPagerAdapter;
import org.forzaverita.iverbs.fragment.TitledFragment;

import java.util.HashMap;
import java.util.Map;

public class TrainActivity extends BaseActivity {

    private MapFragmentsPagerAdapter pagerAdapter;

    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train);

        Map<Integer, TitledFragment> fragments = new HashMap<Integer, TitledFragment>();
        fragments.put(0, new TrainQuizFragment(getString(R.string.train_title_quiz)));
        fragments.put(1, new TrainTextFragment(getString(R.string.train_title_text)));

        pagerAdapter = new MapFragmentsPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_train).setVisible(false);
        return true;
    }

    public void onCLickSelectVerbsToTrain(View view) {
        startActivity(new Intent(this, ScoresActivity.class));
    }

}
