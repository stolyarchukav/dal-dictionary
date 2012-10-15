package forzaverita.brefdic

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.cloudfoundry.runtime.env.CloudEnvironment

class CloudAppContextInitializer extends ApplicationContextInitializer[ConfigurableApplicationContext] {

  private final val LOG : Logger = LoggerFactory.getLogger(classOf[CloudAppContextInitializer])
  
  @Override
  def initialize(applicationContext : ConfigurableApplicationContext) = {
		val env : CloudEnvironment = new CloudEnvironment()
		if (env.getInstanceInfo() != null) {
			LOG.info("Activating cloud profile. Cloud API: " + env.getCloudApiUri())
			applicationContext.getEnvironment().setActiveProfiles("cloud")
		}
		else {
			LOG.info("Activating default profile. OS: " + applicationContext.getEnvironment().getProperty("os.name"))
			applicationContext.getEnvironment().setActiveProfiles("local")
		}
	}
  
}