package forzaverita.daldic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

@PropertySource("classpath:config-local.properties")
@Profile("local")
@Configuration
public class LocalConfig {

	@Autowired
	private Environment env;
	
	@Bean
	public MongoFactoryBean mongo() {
		MongoFactoryBean mongo = new MongoFactoryBean();
		mongo.setHost(env.getProperty("database.host"));
		mongo.setPort(Integer.valueOf(env.getProperty("database.port")));
		return mongo;
	}
	
	@Bean
	public MongoDbFactory mongoDbFactory(Mongo mongo) {
		return new SimpleMongoDbFactory(mongo, env.getProperty("database.user"),
				new UserCredentials(env.getProperty("database.user"), env.getProperty("database.password")));
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
}
