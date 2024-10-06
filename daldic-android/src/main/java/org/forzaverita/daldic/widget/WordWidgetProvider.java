package org.forzaverita.daldic.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.WordActivity;
import org.forzaverita.daldic.service.DalDicService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WordWidgetProvider extends AppWidgetProvider {
	
	private static final String ACTION_WIDGET_PREVIOUS = "org.forzaverita.daldic.ACTION_WIDGET_PREVIOUS";
	private static final String ACTION_WIDGET_NEXT = "org.forzaverita.daldic.ACTION_WIDGET_NEXT";
	
	private DalDicService service;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.word_widget);
		
		WidgetRefreshTask task = getRefreshTask(context);
		if (task == null) {
			task = new RefreshTask(context, appWidgetManager, views);
			service.setWidgetRefreshTask(task);
			((Thread) task).start();
			task.next();
		}
		
		Intent previousIntent = new Intent(context, WordWidgetProvider.class);
		previousIntent.setAction(ACTION_WIDGET_PREVIOUS);
		PendingIntent pendingPreviuosIntent = PendingIntent.getBroadcast(context, 0, previousIntent, PendingIntent.FLAG_MUTABLE);
		views.setOnClickPendingIntent(R.id.widget_left, pendingPreviuosIntent);
		
		Intent nextIntent = new Intent(context, WordWidgetProvider.class);
		nextIntent.setAction(ACTION_WIDGET_NEXT);
		PendingIntent pendingNextIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_MUTABLE);
		views.setOnClickPendingIntent(R.id.widget_right, pendingNextIntent);
		
		appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
	
	private class RefreshTask extends Thread implements WidgetRefreshTask {
		private final RemoteViews views;
		private final AppWidgetManager appWidgetManager;
		private final Context context;
		private final Lock lock = new ReentrantLock();
		
		public RefreshTask(Context context, AppWidgetManager appWidgetManager, 
				RemoteViews views) {
			this.appWidgetManager = appWidgetManager;
			this.views = views; 
			this.context = context;
		}
		
		@Override
		public void run() {
			while (! isInterrupted()) {
				lock.lock();
				try {
					if (service.isAutoRefresh()) {
						next();
					}
				}
				finally {
					lock.unlock();
				}
				try {
					sleep(service.getRefreshInterval() * 1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					interrupt();
				}
			}
		}
		
		@Override
		public void pauseTask() {
			lock.lock();
		}
		
		@Override
		public void resumeTask() {
			lock.unlock();
		}
		
		@Override
		public void next() {
			showLoading();
			String word = service.getNextWord();
			refreshWord(word);
		}
		
		@Override
		public void previous() {
			showLoading();
			String word = service.getPreviousWord();
			refreshWord(word);
		}
		
		private void showLoading() {
			views.setViewVisibility(R.id.widget_word, View.GONE);
			views.setViewVisibility(R.id.widget_loading, View.VISIBLE);
			updateWidget();
		}

		private void refreshWord(String word) {
			Intent intent = new Intent(context, WordActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
            
            views.setViewVisibility(R.id.widget_loading, View.GONE);
            views.setViewVisibility(R.id.widget_word, View.VISIBLE);
            views.setTextViewText(R.id.widget_word, word);
            views.setTextColor(R.id.widget_word, Color.BLACK);
            views.setOnClickPendingIntent(R.id.widget_word, pendingIntent);
            
            updateWidget();
		}
		
		private void updateWidget() {
			ComponentName widget = new ComponentName(context, WordWidgetProvider.class);
			appWidgetManager.updateAppWidget(widget, views);
		}
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		final String action = intent.getAction();
		// previous/next word handling
		if (action.equals(ACTION_WIDGET_PREVIOUS) || action.equals(ACTION_WIDGET_NEXT)) {
			WidgetRefreshTask task = getRefreshTask(context);
			if (task != null) {
				if (action.equals(ACTION_WIDGET_PREVIOUS)) {
					task.previous();
				}
				else {
					task.next();
				}
			}
			updateWidget(context);
		}
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		WidgetRefreshTask task = getRefreshTask(context);
		if (task != null) {
			task.pauseTask();
		}
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		WidgetRefreshTask task = getRefreshTask(context);
		if (task != null) {
			task.resumeTask();
			task.next();
		}
	}
	
	private WidgetRefreshTask getRefreshTask(Context context) {
		if (service == null) {
			service = ((DalDicService) context.getApplicationContext());
		}
		return service.getWidgetRefreshTask();
	}
	
	private void updateWidget(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context, WordWidgetProvider.class);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		onUpdate(context, appWidgetManager, appWidgetIds);
	}
		
}
