package forzaverita.brefdic.config

import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import forzaverita.brefdic.repo.RepoPackageMarker
import forzaverita.brefdic.rest.RestPackageMarker
import org.springframework.context.annotation.Bean

@Import(Array(classOf[LocalConfig], classOf[CloudConfig]))
@ImportResource(Array("classpath:/app-context.xml"))
@ComponentScan(basePackageClasses = Array(classOf[RepoPackageMarker], classOf[RestPackageMarker]))
@Configuration
class AppConfig {

	@Bean
	def dummy() : String = "Test"
  
}