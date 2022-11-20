package com.openlap.AnalyticsMethods.exceptions;

import com.openlap.OpenLAPAnalyticsFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom Exception to be thrown whenever the AnalyticsMethods Loader does not work properly.
 */
public class AnalyticsMethodLoaderException extends RuntimeException {

	private static final Logger log =
			LoggerFactory.getLogger(OpenLAPAnalyticsFramework.class);

	public AnalyticsMethodLoaderException(String message) {
		super(message);
		log.error("Analytics Method could not be loaded: " + message);
	}

}
