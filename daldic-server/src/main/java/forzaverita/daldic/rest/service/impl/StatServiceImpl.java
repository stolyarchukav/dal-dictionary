package forzaverita.daldic.rest.service.impl;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import forzaverita.daldic.core.event.BaseEvents;
import forzaverita.daldic.core.event.SearchEvents;
import forzaverita.daldic.core.event.WordEvents;
import forzaverita.daldic.data.BaseEvent;
import forzaverita.daldic.data.BookmarkWordEvent;
import forzaverita.daldic.data.FullSearchEvent;
import forzaverita.daldic.data.OpenWordEvent;
import forzaverita.daldic.data.OpenWordWidgetEvent;
import forzaverita.daldic.data.SearchEvent;
import forzaverita.daldic.repo.StatRepository;
import forzaverita.daldic.rest.service.StatService;

@Service
@Scope("request")
public class StatServiceImpl implements StatService {

	private static final Logger LOG = LoggerFactory.getLogger(StatServiceImpl.class);
	
	@Autowired
	private StatRepository repository;
	
	@Override
	public Response searchEvents(SearchEvents events) {
		LOG.info("Search events: " + events.toString());
		SearchEvent event = new SearchEvent();
		fillBaseEvent(events, event);
		event.setSearchStrings(event.getSearchStrings());
		repository.saveEvent(event);
		return Response.ok().build();
	}
	
	@Override
	public Response fullSearchEvents(SearchEvents events) {
		LOG.info("Full search events: " +events.toString());
		FullSearchEvent event = new FullSearchEvent();
		fillBaseEvent(events, event);
		event.setSearchStrings(event.getSearchStrings());
		repository.saveEvent(event);
		return Response.ok().build();
	}

	private void fillBaseEvent(BaseEvents events, BaseEvent event) {
		event.setCountry(events.getCountry());
		event.setDeviceModel(events.getDeviceModel());
		event.setEventDate(events.getEventDate());
	}
	
	@Override
	public Response openWordEvents(WordEvents events) {
		LOG.info("Open word events: " + events.toString());
		OpenWordEvent event = new OpenWordEvent();
		fillBaseEvent(events, event);
		event.setWordIds(event.getWordIds());
		repository.saveEvent(event);
		return Response.ok().build();
	}
	
	@Override
	public Response openWordWidgetEvents(WordEvents events) {
		LOG.info("Open word widget events: " + events.toString());
		OpenWordWidgetEvent event = new OpenWordWidgetEvent();
		fillBaseEvent(events, event);
		event.setWordIds(event.getWordIds());
		repository.saveEvent(event);
		return Response.ok().build();
	}
	
	@Override
	public Response bookmarkWordEvents(WordEvents events) {
		LOG.info("Bookmark word events: " + events.toString());
		BookmarkWordEvent event = new BookmarkWordEvent();
		fillBaseEvent(events, event);
		event.setWordIds(event.getWordIds());
		repository.saveEvent(event);
		return Response.ok().build();
	}
	
	@Override
	public Response getPopularWords() {
		LOG.info("request for popular words");
		return Response.ok(repository.getPopularWords()).build();
	}
	
	@Override
	public Response getSearchRate() {
		LOG.info("request for search rate");
		return Response.ok(repository.getSearchRate()).build();
	}
	
}
