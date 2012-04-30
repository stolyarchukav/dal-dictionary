package forzaverita.daldic.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import forzaverita.daldic.data.OpenWordEvent;
import forzaverita.daldic.data.SearchEvent;

@Path("/statistic")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StatService {

	@POST
	@Path("/search")
	Response searchEvent(SearchEvent event);

	@POST
	@Path("/full_search")
	Response fullSearchEvent(SearchEvent event);

	@POST
	@Path("/open_word")
	Response openWordEvent(OpenWordEvent event);
	
	@GET
	@Path("/popular_words")
	
	Response getPopularWords();

	@GET
	@Path("/search_rate")
	Response getSearchRate();
}
