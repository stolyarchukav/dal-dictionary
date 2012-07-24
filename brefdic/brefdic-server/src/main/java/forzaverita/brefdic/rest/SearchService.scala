package forzaverita.brefdic.rest

import java.util.Collection
import javax.ws.rs.Produces
import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.GET
import forzaverita.brefdic.model.Word
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.DELETE
import org.springframework.security.access.prepost.PreAuthorize
import forzaverita.brefdic.model.wrapper.WordWrapper

@Path("/search/")
@Consumes(Array(MediaType.APPLICATION_JSON))
@Produces(Array(MediaType.APPLICATION_JSON))
trait SearchService {
  
  @GET
  @Path("/begin/{begin}")
  def getWordsBeginWith(@PathParam("begin") begin : String) : WordWrapper
  
  @GET
  @Path("/fullsearch/{query}")
  def getWordsFullSearch(@PathParam("query") query : String) : WordWrapper

}
