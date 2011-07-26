package org.forzaverita.daldic;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

public class WordWidgetProvider extends AppWidgetProvider {
	
	private static String ACTION_WIDGET_REFRESH = "org.forzaverita.daldic.ACTION_WIDGET_REFRESH";
	
	private DalDicService service;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.word_widget);
		
		if (service == null) {
			service = ((DalDicService) context.getApplicationContext());
		}
		WidgetRefreshTask task = service.getWidgetRefreshTask();
		if (task == null) {
			task = new RefreshTask(context, appWidgetManager, views);
			service.setWidgetRefreshTask(task);
			new Thread((Runnable) task).start();
		}
		else {
			task.refresh();
		}
		
		Intent refreshIntent = new Intent(context, WordWidgetProvider.class);
		refreshIntent.setAction(ACTION_WIDGET_REFRESH);
		PendingIntent pendingRefreshIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
		views.setOnClickPendingIntent(R.id.widget_refresh, pendingRefreshIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
	
	private class RefreshTask implements Runnable, WidgetRefreshTask {
		RemoteViews views;
		AppWidgetManager appWidgetManager;
		Context context;
		
		public RefreshTask(Context context, AppWidgetManager appWidgetManager, 
				RemoteViews views) {
			this.appWidgetManager = appWidgetManager;
			this.views = views; 
			this.context = context;
		}
		
		@Override
		public void run() {
			while (true) {
				refresh();
				try {
					Thread.sleep(10000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					Thread.interrupted();
				}
			}
		}
		
		@Override
		public void refresh() {
			String word = service.getRandomWord();
			Intent intent = new Intent(context, WordActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            views.setTextViewText(R.id.widget_word, word);
            views.setTextColor(R.id.widget_word, Color.BLACK);
            views.setOnClickPendingIntent(R.id.widget_word, pendingIntent);
            
            ComponentName widget = new ComponentName(context, WordWidgetProvider.class);
            appWidgetManager.updateAppWidget(widget, views);
		}
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		}
		else {
			super.onReceive(context, intent);
		}
		if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		    ComponentName thisAppWidget = new ComponentName(context, WordWidgetProvider.class);
		    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		    onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}
		
}
