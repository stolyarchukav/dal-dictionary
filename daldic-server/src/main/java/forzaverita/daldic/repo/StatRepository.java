package forzaverita.daldic.repo;

import java.util.Map;

import forzaverita.daldic.data.BaseEvent;

public interface StatRepository {

	void saveEvent(BaseEvent event);

	Map<Long, Long> getPopularWords();

	Map<String, Float> getSearchRate();

}
