package org.forzaverita.iverbs;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import org.forzaverita.iverbs.fragment.CommonPagerAdapter;
import org.forzaverita.iverbs.fragment.TitledFragment;
import org.forzaverita.iverbs.train.TrainQuizFragment;
import org.forzaverita.iverbs.train.TrainTextFragment;

import java.util.HashMap;
import java.util.Map;

public class TrainActivity extends BaseActivity {

    private CommonPagerAdapter pagerAdapter;

    private ViewPager viewPager;

    private final Map<Integer, TitledFragment> fragments = new HashMap<Integer, TitledFragment>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train);

        fragments.put(0, new TrainQuizFragment(getString(R.string.train_title_quiz)));
        fragments.put(1, new TrainTextFragment(getString(R.string.train_title_text)));

        pagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_train).setVisible(false);
        return true;
    }

}
