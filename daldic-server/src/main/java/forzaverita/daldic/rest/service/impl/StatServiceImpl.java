package forzaverita.daldic.rest.service.impl;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import forzaverita.daldic.data.OpenWordEvent;
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
	public Response searchEvent(SearchEvent event) {
		LOG.info(event.toString());
		repository.saveEvent(event);
		return Response.ok().build();
	}
	
	@Override
	public Response fullSearchEvent(SearchEvent event) {
		LOG.info(event.toString());
		repository.saveEvent(event);
		return Response.ok().build();
	}
	
	@Override
	public Response openWordEvent(OpenWordEvent event) {
		LOG.info(event.toString());
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
