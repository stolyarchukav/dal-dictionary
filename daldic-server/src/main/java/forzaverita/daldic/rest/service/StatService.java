package forzaverita.daldic.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import forzaverita.daldic.core.event.SearchEvents;
import forzaverita.daldic.core.event.WordEvents;

@Path("/statistic")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StatService {

	@POST
	@Path("/search")
	Response searchEvents(SearchEvents events);

	@POST
	@Path("/full_search")
	Response fullSearchEvents(SearchEvents events);

	@POST
	@Path("/open_word")
	Response openWordEvents(WordEvents events);
	
	@POST
	@Path("/open_word_widget")
	Response openWordWidgetEvents(WordEvents events);
	
	@POST
	@Path("/bookmark_word")
	Response bookmarkWordEvents(WordEvents events);
	
	@GET
	@Path("/popular_words")
	Response getPopularWords();

	@GET
	@Path("/search_rate")
	Response getSearchRate();
}
