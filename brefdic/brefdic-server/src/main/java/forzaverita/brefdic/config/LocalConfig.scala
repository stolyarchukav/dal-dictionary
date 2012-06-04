package forzaverita.brefdic.config

import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean

@PropertySource(Array("classpath:config-local.properties"))
@Profile(Array("local"))
@Configuration
class LocalConfig {
  
	@Autowired
	private val env : Environment = null
	
	@Bean
	def test() : String = {
		env.getActiveProfiles()(0)
	}

}