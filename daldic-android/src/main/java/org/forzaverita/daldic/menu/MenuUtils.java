package org.forzaverita.daldic.menu;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import org.forzaverita.daldic.DalDicActivity;
import org.forzaverita.daldic.R;
import org.forzaverita.daldic.history.BookmarksActivity;
import org.forzaverita.daldic.history.HistoryActivity;
import org.forzaverita.daldic.preferences.AppPreferenceActivity;

public class MenuUtils {

    public static boolean createOptionsMenu(Menu menu, AppCompatActivity activity) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public static boolean createOptionsMenuNoBar(Menu menu, AppCompatActivity activity) {
        return createOptionsMenu(menu, activity);
    }
	
	public static boolean optionsItemSelected(MenuItem item, AppCompatActivity activity) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            activity.onBackPressed();
            return true;
        } else if (itemId == R.id.menu_settings) {
            activity.startActivity(new Intent(activity, AppPreferenceActivity.class));
            return true;
        } else if (itemId == R.id.menu_search) {
            activity.onSearchRequested();
            return true;
        } else if (itemId == R.id.menu_history) {
            activity.startActivity(new Intent(activity, HistoryActivity.class));
            return true;
        } else if (itemId == R.id.menu_bookmarks) {
            activity.startActivity(new Intent(activity, BookmarksActivity.class));
            return true;
        } else if (itemId == R.id.menu_home) {
            activity.startActivity(new Intent(activity, DalDicActivity.class));
            return true;
        }
		return false;
	}
	
}
