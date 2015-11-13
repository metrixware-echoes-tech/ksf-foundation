package fr.echoes.lab.foremanclient;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AddVersionHeaderRequestFilter implements ClientRequestFilter{
    final static Logger LOGGER = LoggerFactory.getLogger(AddVersionHeaderRequestFilter.class);
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        // add header to define the api version
        requestContext.getHeaders().add("Accept", "version=2");

        // log all headers
        final MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        final Iterator<?> it = headers.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<?, ?> pair = (Map.Entry<?, ?>)it.next();
            LOGGER.debug("Header name: {}, value: {}.", pair.getKey(), pair.getValue());
        }

    }

}
