package org.forzaverita.iverbs.fragment;

import android.support.v4.app.Fragment;

public abstract class TitledFragment extends Fragment {

    private String title;

    public TitledFragment(String title) {
        this.title = title;
    }

    protected TitledFragment() {
    }

    public String getTitle() {
        return title;
    }

}
