package org.forzaverita.daldic;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.data.Word;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class WordActivity extends Activity {
	
	private static final String UTF_8 = "UTF-8";
	private static final String FILE_ANDROID_ASSET = "file:///android_asset/";
	private static final String WORD_TEMPLATE_HTML = "word_template.html";
	
	private DalDicService service;
	private Date lastPreferencesCheck = new Date();
	
	@Override
	protected void onResume() {
		super.onResume();
		if (service.isPreferencesChanged(lastPreferencesCheck)) {
			lastPreferencesCheck = new Date();
			onCreate(null);
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        service = (DalDicService) getApplicationContext();
        configureActivity();
    }

	private void configureActivity() {
		boolean fromWidget = false;
        Word word;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	int wordId = (Integer) extras.get(Constants.WORD_ID);
        	word = service.getWord(wordId);
        }
        else {
        	word = service.getCurrentWord();
        	fromWidget = true;
        }
        configureWord(word);
        configureGotoMain(fromWidget);
	}

	private void configureWord(Word word) {
		int wordId = word.getId();
        String description = buildDescription(word);
    	service.addToHistory(wordId, word.getWord());
    	configureTopPanel(wordId, word.getWord());
        configureWordView(description);
	}

	private String buildDescription(Word word) {
		String description = null;
		StringBuilder descBuilder = new StringBuilder();
		if (word.getDescription() != null) {
			descBuilder.append(word.getDescription());
		} 
		if (word.getDescriptionRef() != null) {
			if (descBuilder.length() > 0) {
				descBuilder.append("<hr>");
			}
			descBuilder.append(word.getDescriptionRef());
		}
		if (descBuilder.length() > 0) {
			description = descBuilder.toString();
		}
        description = description.replaceAll("[|]", "<p>");
		return description;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return MenuUtils.createOptionsMenu(menu, this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtils.optionsItemSelected(item, this);
	}

	private void configureTopPanel(final Integer wordId, final String word) {
		final AtomicBoolean bookmarked = new AtomicBoolean(service.isBookmarked(wordId));
		final Button buttonBookmark = (Button) findViewById(R.id.bookmark);
		configureBookmark(bookmarked.get(), buttonBookmark);
		buttonBookmark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bookmarked.get()) {
					service.removeBookmark(wordId);					
				}
				else {
					service.addBookmark(wordId, word);
				}
				bookmarked.set(! bookmarked.get());
				configureBookmark(bookmarked.get(), buttonBookmark);
			}
		});
		final Button buttonPrevious = (Button) findViewById(R.id.word_previous);
		buttonPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				configureWord(getPreviousWord(wordId));
			}
		});
		final Button buttonNext = (Button) findViewById(R.id.word_next);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				configureWord(getNextWord(wordId));
			}
		});
	}
	
	private Word getPreviousWord(int wordId) {
		if (wordId <= 0) {
			wordId = Constants.WORDS_COUNT;
		}
		Word word = service.getWord(--wordId);
		return word != null ? word : getPreviousWord(wordId - 1);
	}

	protected Word getNextWord(Integer wordId) {
		if (wordId > Constants.WORDS_COUNT) {
			wordId = 1;
		}
		Word word = service.getWord(++wordId);
		return word != null ? word : getNextWord(wordId + 1);
	}
	
	private void configureBookmark(final boolean bookmarked, Button button) {
		button.setBackgroundResource(bookmarked ? R.drawable.bookmark_on : R.drawable.bookmark_off);
	}
	
	private void configureWordView(String description) {
		if (description != null) {
        	WebView text = (WebView) findViewById(R.id.word_text);
        	text.setBackgroundColor(0x00000000);
        	text.getSettings().setDefaultFontSize(25);
        	text.getSettings().setSupportZoom(true);
        	text.getSettings().setBuiltInZoomControls(true);
        	text.getSettings().setDefaultTextEncodingName(UTF_8);
        	text.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        	String data = "";
        	AssetManager assetManager = getApplicationContext().getResources().getAssets();
        	try {
        	    InputStream inputStream = assetManager.open(WORD_TEMPLATE_HTML);
        	    byte[] b = new byte[inputStream.available()];
        	    inputStream.read(b);
        	    String fontName = service.resolveTypeface(service.getFont()).name();
        	    data = String.format(new String(b), fontName, fontName, fontName,
        	    		service.getWordTextAlign(), description);
        	    inputStream.close();
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        	}
        	text.loadDataWithBaseURL(FILE_ANDROID_ASSET + WORD_TEMPLATE_HTML, data,
        			"text/html", UTF_8, "about:blank");
        }
	}

	private void configureGotoMain(boolean fromWidget) {
		Button button = (Button) findViewById(R.id.word_goto_main);
        if (fromWidget) {
        	button.setVisibility(View.VISIBLE);
        	button.setTypeface(service.getFont());
        	button.setTextColor(Color.BLACK);
        	button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(WordActivity.this, DalDicActivity.class);
					startActivity(intent);
				}
			});
        }
        else {
        	button.setVisibility(View.GONE);
        }
	}
	
}