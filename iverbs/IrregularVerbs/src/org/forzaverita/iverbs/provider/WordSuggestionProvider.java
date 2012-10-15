package org.forzaverita.iverbs.provider;

import org.forzaverita.iverbs.service.AppService;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class WordSuggestionProvider extends ContentProvider {
	
	private AppService service;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		service = (AppService) getContext().getApplicationContext();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if  (selectionArgs.length > 0 && selectionArgs[0] != null) {
			cursor = service.getCursorVerbsContains(selectionArgs[0]);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,	String[] selectionArgs) {
		return 0;
	}
	
}
