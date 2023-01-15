package org.forzaverita.daldic.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.forzaverita.daldic.service.DalDicService;

public class WordSuggestionProvider extends ContentProvider {
	
	private DalDicService service;

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
		service = (DalDicService) getContext().getApplicationContext();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if  (selectionArgs.length != 0) {
			String searchString = selectionArgs[0];
			if (searchString != null && !searchString.isEmpty()) {
				cursor = service.getCursorOfWordsBeginWith(searchString);
			}
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
