package fr.echoes.labs.foremanclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
public class ClientErrorResponseFilter implements ClientResponseFilter {
	final static Logger LOGGER = LoggerFactory.getLogger(ClientErrorResponseFilter.class);


	@Override
	public void filter(ClientRequestContext requestContext,
			ClientResponseContext responseContext) throws IOException {

		final int status = responseContext.getStatus();

		if (status >= 400) {

			final String logMessage = createLogMessage(requestContext, responseContext);
			LOGGER.error(logMessage);

		} else if (LOGGER.isDebugEnabled()) {

			final String logMessage = createLogMessage(requestContext, responseContext);
			LOGGER.debug(logMessage);
		}

	}


	/**
	 * Extracts the response body.
	 * After this method the response entity InputStream is still available.
	 */
	private String getResponseBody(ClientResponseContext responseContext) throws IOException {

		final InputStream entityStream = responseContext.getEntityStream();
		final byte[] bytes = IOUtils.toByteArray(entityStream);
		final ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
		responseContext.setEntityStream(baos);

		return new String(bytes, "UTF8");

	}

	/**
	 * Returns a message with the request URL and the request body response.
	 */
	private String createLogMessage(ClientRequestContext requestContext, ClientResponseContext responseContext) {

		try {

			final String method = requestContext.getMethod();
			final URI uri = requestContext.getUri();
			final int status = responseContext.getStatus();
			final String responseBody = getResponseBody(responseContext);
			return String.format("[%s] [%s]\nResponse with status %d was returned. Response body: %s.", method, uri.toString(), status, responseBody);

		} catch (final IOException e) {
			LOGGER.error("Failed to get response body", e);
			return null;
		}

	}
}
