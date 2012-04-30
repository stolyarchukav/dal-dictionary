package forzaverita.daldic.repo.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;

import forzaverita.daldic.data.BaseEvent;
import forzaverita.daldic.data.aggregates.StringLongResult;
import forzaverita.daldic.data.aggregates.LongLongResult;
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
			map.put(count.getId(), count.getValue());
		}
		return map;
	}
	
	@Override
	public Map<String, Float> getSearchRate() {
		MapReduceResults<StringLongResult> searchCounts = mongoOperations.mapReduce("searchEvent", 
				"classpath:map-reduce/search_rate_map.js", "classpath:map-reduce/search_rate_reduce.js", 
				StringLongResult.class);
		Map<String, Long> map = new HashMap<String, Long>();
		StringLongResult max = new StringLongResult();
		for (StringLongResult searchCount : searchCounts) {
			long count = searchCount.getValue();
			if (count > max.getValue()) {
				max = searchCount;
			}
			map.put(searchCount.getId(), searchCount.getValue());			
		}
		long maxCount = max.getValue();
		Map<String, Float> result = new HashMap<String, Float>();
		for (Entry<String, Long> entry : map.entrySet()) {
			if (entry.getKey() != null) {
				result.put(entry.getKey(), (float) (entry.getValue().doubleValue() / maxCount));
			}
			else {
				LOG.warn("null key as result of search rate map-reduce. Entry = " + entry);
			}
		}
		return result;
	}
	
}
