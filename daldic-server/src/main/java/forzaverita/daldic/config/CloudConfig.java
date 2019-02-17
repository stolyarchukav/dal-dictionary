package forzaverita.daldic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@ImportResource("classpath:META-INF/cloud/cloudfoundry-auto-reconfiguration-context.xml")
@Profile("cloud")
@Configuration
public class CloudConfig {

}
