package org.forzaverita.daldic.menu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.forzaverita.daldic.DalDicActivity;
import org.forzaverita.daldic.R;
import org.forzaverita.daldic.history.BookmarksActivity;
import org.forzaverita.daldic.history.HistoryActivity;
import org.forzaverita.daldic.preferences.AppPreferenceActivity;

public class MenuUtils {

    public static boolean createOptionsMenu(Menu menu, Activity activity) {
        return createOptionsMenu(menu, activity, true);
    }

    public static boolean createOptionsMenuNoBar(Menu menu, Activity activity) {
        return createOptionsMenu(menu, activity, false);
    }

	private static boolean createOptionsMenu(Menu menu, Activity activity, boolean actionBar) {
        if (actionBar) {
            ActionBar ab = activity.getActionBar();
            if (ab != null) {
                ab.setHomeButtonEnabled(true);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setTitle(activity.getTitle());
                ab.show();
            }
        }
		MenuInflater inflater = activity.getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public static boolean optionsItemSelected(MenuItem item, Activity activity) {
		switch (item.getItemId()) {
            case android.R.id.home :
                activity.startActivity(new Intent(activity, DalDicActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
			case R.id.menu_settings :
				activity.startActivity(new Intent(activity, AppPreferenceActivity.class));
				break;
			case R.id.menu_seacrh :
				activity.onSearchRequested();
				break;
			case R.id.menu_history :
				activity.startActivity(new Intent(activity, HistoryActivity.class));
				break;
			case R.id.menu_bookmarks :
				activity.startActivity(new Intent(activity, BookmarksActivity.class));
				break;
			case R.id.menu_home:
				activity.startActivity(new Intent(activity, DalDicActivity.class));
				break;
		}
		return true;
	}
	
}
