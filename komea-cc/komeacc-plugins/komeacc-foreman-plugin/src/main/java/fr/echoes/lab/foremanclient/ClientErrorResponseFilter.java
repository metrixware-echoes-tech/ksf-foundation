package fr.echoes.lab.foremanclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
public class ClientErrorResponseFilter implements ClientResponseFilter {
	final static Logger LOGGER = LoggerFactory.getLogger(ClientErrorResponseFilter.class);

	@Override
	public void filter(ClientRequestContext requestContext,
			ClientResponseContext responseContext) throws IOException {
		if(responseContext.getStatus() != 200){
			final StringBuilder errMsg = new StringBuilder();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(responseContext.getEntityStream()));
			String line;
			while((line = reader.readLine()) != null){
				errMsg.append(line);
			}
			LOGGER.error("Response with error status {} was returned. Response body: {}.",
					Integer.toString(responseContext.getStatus()), errMsg);
		}
	}
}
