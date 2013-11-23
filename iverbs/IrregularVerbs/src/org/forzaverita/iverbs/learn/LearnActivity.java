package org.forzaverita.iverbs.learn;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.forzaverita.iverbs.BaseActivity;
import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.data.Verb;

public class LearnActivity extends BaseActivity {

    private LearnPagerAdapter pagerAdapter;

    private ViewPager viewPager;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn);

        pagerAdapter = new LearnPagerAdapter(getSupportFragmentManager(), service);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt(Constants.VERB_ID);
            viewPager.setCurrentItem(id - 1);
        }
    }

    public void onClickPrevious(View view) {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(service.getVerbsCount() - 1);
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void onClickNext(View view) {
        if (viewPager.getCurrentItem() == service.getVerbsCount() - 1) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void speakForm1(View view) {
        speak(getCurrentVerb().getForm1());
    }

    public void speakForm2(View view) {
        speak(getCurrentVerb().getForm2());
    }

    public void speakForm3(View view) {
        speak(getCurrentVerb().getForm3());
    }

    private Verb getCurrentVerb() {
        return service.getVerb(viewPager.getCurrentItem() + 1);
    }

}
