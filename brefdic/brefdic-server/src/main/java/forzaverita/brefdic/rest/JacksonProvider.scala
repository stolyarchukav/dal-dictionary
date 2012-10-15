package forzaverita.brefdic.rest

import java.lang.Override
import javax.ws.rs.ext.Provider
import javax.ws.rs.ext.ContextResolver
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.annotation.JsonInclude

@Component
@Provider
class JacksonProvider extends ContextResolver[ObjectMapper] {

  @Override 
  def getContext(clazz : Class[_]) : ObjectMapper = {
	  new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
  }
  
}