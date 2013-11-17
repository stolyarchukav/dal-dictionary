package org.forzaverita.iverbs.fragment;

import android.support.v4.app.Fragment;

public class TitledFragment extends Fragment {

    private final String title;

    public TitledFragment(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
