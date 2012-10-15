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
import org.springframework.security.access.prepost.PreAuthorize

@Path("/words")
@Consumes(Array(MediaType.APPLICATION_JSON))
@Produces(Array(MediaType.APPLICATION_JSON))
trait WordsService {
  
  @GET
  @Path("/verbose")
  @PreAuthorize("hasPermission(#user, 'read_app')")
  def getWords(@QueryParam("user") user : String) : Collection[Word]

  @GET
  def getIndexWords : Collection[Word]
  
  @GET
  @Path("/{page}/{size}")
  def getIndexWords(@PathParam("page") page : Int, @PathParam("size") size : Int) : Collection[Word]
  
  @GET
  @Path("/count")
  @PreAuthorize("hasPermission(#user, 'read_app')")
  def getWordsCount(@QueryParam("user") user : String) : Long
  
}
