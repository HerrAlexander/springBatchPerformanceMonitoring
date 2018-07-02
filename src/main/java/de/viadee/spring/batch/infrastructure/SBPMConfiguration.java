package de.viadee.spring.batch.infrastructure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SBPMConfiguration {

	private static final Logger LOG = LoggingWrapper.getLogger(SBPMConfiguration.class);

	private String username, password, url, driver;
	private boolean trackanomaly;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public boolean isTrackanomaly() {
		return trackanomaly;
	}

	SBPMConfiguration() {
		LOG.debug("SPBMConfiguration built");
		setProperties();
	}

	// TODO: Fix the Exception Handling
	// Accessing the Files was initially using direct file access. Then it was
	// changed to using the Resource Object.
	// The Exception handling is set to handle direct file access exceptions. It has
	// to be changed to comply
	// with the Resource Object. What you see is a HotFixed.

	/**
	 * Try to read the specified SpringBatchMonitoring.properties file. If an error
	 * occurs, use fallback defaults
	 */
	protected void setProperties() {

		// Fill in Default Values
		InputStream input = null;
		try {
			final Resource resource = new ClassPathResource("springBatchMonitoringDefault.properties");
			input = resource.getInputStream();
			setConfigProperties(input);
		} catch (final FileNotFoundException e1) {
			LOG.warn("SpringBatchMonitoringDefault.properties file not found.");
			LOG.warn(e1);
		} catch (final IOException e1) {
			LOG.warn("Opening springBatchMonitoringDefault.properties threw an IO Exception.");
			LOG.warn(e1);
		} catch (final NullPointerException e1) {
			LOG.warn(
					"Using the Fallback Default values (springbatchmonitoringDefault.properties) threw an NullPointerException!.");
			LOG.warn(e1);
		}

		// Override with user confog properties
		try {
			final Resource resource = new ClassPathResource("SpringBatchMonitoring.properties");
			input = resource.getInputStream();
			setConfigProperties(input);

		} catch (final NullPointerException e) {
			LOG.warn(
					"SpringBatchMonitoring.properties file not found. Falling back to default values (file inside JAR)!");
			LOG.warn(e);

		} catch (final IOException e) {
			LOG.warn(
					"SpringBatchMonitoring.properties file is not accessible / malformed. Falling back to default values");
			LOG.warn(e);
		}

	}

	protected void setConfigProperties(final InputStream input) throws IOException {
		final Properties properties = new Properties();
		properties.load(input);

		String propUser = properties.getProperty("db.username");
		username = propUser.isEmpty() ? username : propUser;

		String propPassw = properties.getProperty("db.password");
		password = propPassw.isEmpty() ? password : propPassw;

		String propDriver = properties.getProperty("db.driver");
		driver = propDriver.isEmpty() ? driver : propDriver;

		String propUrl = properties.getProperty("db.url");
		url = propUrl.isEmpty() ? url : propUrl;

		boolean propAnomaly = Boolean.parseBoolean(properties.getProperty("db.anomalydetection"));
		trackanomaly = propAnomaly ? trackanomaly : propAnomaly;

	}
}
