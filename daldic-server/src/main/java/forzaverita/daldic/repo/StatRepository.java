package forzaverita.daldic.repo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import forzaverita.daldic.data.BaseEvent;
import forzaverita.daldic.data.OpenWordEvent;
import forzaverita.daldic.data.SearchEvent;

public interface StatRepository {

	void saveEvent(BaseEvent event);

	Map<Long, Long> getPopularWords();

	Map<String, Long> getSearchRate();

	List<SearchEvent> getEventsSearch(Date begin, Date end);

	List<SearchEvent> getSearchEvents(Date begin, Date end);

	List<OpenWordEvent> getEventsOpenWord();

	Long getEventsOpenWordCount();
	
}
