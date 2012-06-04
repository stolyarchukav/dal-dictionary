package forzaverita.brefdic.config

import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Configuration

@ImportResource(Array("classpath:META-INF/cloud/cloudfoundry-auto-reconfiguration-context.xml"))
@Profile(Array("cloud"))
@Configuration
class CloudConfig {

}