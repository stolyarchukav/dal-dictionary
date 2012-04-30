package forzaverita.daldic;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class CloudApplicationContextInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CloudApplicationContextInitializer.class); 

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		CloudEnvironment env = new CloudEnvironment();
		if (env.getInstanceInfo() != null) {
			LOG.info("Activating cloud profile. Cloud API: " + env.getCloudApiUri());
			applicationContext.getEnvironment().setActiveProfiles("cloud");
		}
		else {
			LOG.info("Activating default profile. OS: " + applicationContext.getEnvironment().getProperty("os.name"));
			applicationContext.getEnvironment().setActiveProfiles("local");
		}
	}

}
