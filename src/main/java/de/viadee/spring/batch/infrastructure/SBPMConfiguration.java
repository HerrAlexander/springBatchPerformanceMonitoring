package de.viadee.spring.batch.infrastructure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

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

	public boolean trackAnomaly() {
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
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try {
			input = classloader.getResourceAsStream("springBatchMonitoringDefault.properties");
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

		// Override with user config properties
		try {
			// final Resource resource = new
			// ClassPathResource("springBatchMonitoring.properties");
			input = classloader.getResourceAsStream("springBatchMonitoring.properties");
			// input = resource.getInputStream();
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

		username = properties.getProperty("db.username", username);
		password = properties.getProperty("db.password", password);
		driver = properties.getProperty("db.driver", driver);
		url = properties.getProperty("db.url", url);
		trackanomaly = Boolean
				.parseBoolean(properties.getProperty("db.anomalydetection", String.valueOf(trackanomaly)));

	}
}
