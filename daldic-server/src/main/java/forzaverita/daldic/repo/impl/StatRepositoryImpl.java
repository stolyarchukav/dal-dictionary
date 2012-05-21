package forzaverita.daldic.repo.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;

import forzaverita.daldic.data.BaseEvent;
import forzaverita.daldic.data.OpenWordEvent;
import forzaverita.daldic.data.SearchEvent;
import forzaverita.daldic.data.aggregates.LongLongResult;
import forzaverita.daldic.data.aggregates.StringLongResult;
import forzaverita.daldic.repo.StatRepository;

@Repository
public class StatRepositoryImpl implements StatRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(StatRepositoryImpl.class);

	@Autowired
	private MongoOperations mongoOperations;
	
	@Override
	public void saveEvent(BaseEvent event) {
		if (event.getId() == null) {
			event.setId(UUID.randomUUID());
		}
		mongoOperations.insert(event);
	}
	
	@Override
	public Map<Long, Long> getPopularWords() {
		MapReduceResults<LongLongResult> counts = mongoOperations.mapReduce("openWordEvent", 
				"classpath:map-reduce/word_map.js", "classpath:map-reduce/word_reduce.js", 
				LongLongResult.class);
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (LongLongResult count : counts) {
			if (count.getValue() > 1) {
				map.put(count.getId(), count.getValue());
			}
		}
		return map;
	}
	
	@Override
	public Map<String, Long> getSearchRate() {
		MapReduceResults<StringLongResult> searchCounts = mongoOperations.mapReduce("searchEvent", 
				"classpath:map-reduce/search_rate_map.js", "classpath:map-reduce/search_rate_reduce.js", 
				StringLongResult.class);
		final Map<String, Long> map = new HashMap<String, Long>();
		for (StringLongResult searchCount : searchCounts) {
			map.put(searchCount.getId(), searchCount.getValue());			
		}
		Map<String, Long> result = new TreeMap<String, Long>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return map.get(o2).compareTo(map.get(o1)) < 0 ? -1 : 1;
			}
		});
		result.putAll(map);
		return result;
	}
	
	@Override
	public List<SearchEvent> getEventsSearch(Date begin, Date end) {
		/*Criteria criteria = where("true");
		if (begin != null) {
			criteria = criteria.and("eventDate").gt(begin);
		}
		if (end != null) {
			criteria = criteria.and("eventDate").lt(end);
		}
		return mongoOperations.find(query(criteria), BaseEvent.class);*/
		return mongoOperations.findAll(SearchEvent.class);
	}
	
	@Override
	public List<SearchEvent> getSearchEvents(Date begin, Date end) {
		return mongoOperations.find(query(where("eventDate").gt(begin).and("eventDate").lt(end)), SearchEvent.class);
	}
	
	@Override
	public List<OpenWordEvent> getEventsOpenWord() {
		return mongoOperations.findAll(OpenWordEvent.class);
	}
	
	@Override
	public Long getEventsOpenWordCount() {
		return mongoOperations.count(null, OpenWordEvent.class);
	}
	
}
