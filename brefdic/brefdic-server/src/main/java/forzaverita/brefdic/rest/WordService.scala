package forzaverita.brefdic.rest

import javax.ws.rs.Produces
import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.GET
import forzaverita.brefdic.model.Word
import javax.ws.rs.PathParam

@Path("/word/")
@Consumes(Array(MediaType.APPLICATION_JSON))
@Produces(Array(MediaType.APPLICATION_JSON))
trait WordService {
  
  @GET
  @Path("{id}")
  def getWord(@PathParam("id") id : Integer) : Word
  
  @GET
  @Path("test")
  def test() : String
  
}