package forzaverita.daldic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import forzaverita.daldic.repo.RepoPackageMarker;
import forzaverita.daldic.rest.RestPackageMarker;

@Import({LocalConfig.class, CloudConfig.class})
@ImportResource("classpath:/app-context.xml")
@ComponentScan(basePackageClasses = {RepoPackageMarker.class, RestPackageMarker.class})
@Configuration
public class AppConfig {
	
	@Bean
	public MongoOperations mongoTemplate(MongoDbFactory mongo) {
		return new MongoTemplate(mongo);
	}
	
}
