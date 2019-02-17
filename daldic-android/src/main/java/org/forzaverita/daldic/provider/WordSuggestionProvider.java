package org.forzaverita.daldic.provider;

import org.forzaverita.daldic.service.DalDicService;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class WordSuggestionProvider extends ContentProvider {
	
	private DalDicService service;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
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
		if  (selectionArgs.length > 0 && selectionArgs[0] != null && selectionArgs[0].length() > 1) {
			cursor = service.getCursorOfWordsBeginWith(selectionArgs[0]);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
