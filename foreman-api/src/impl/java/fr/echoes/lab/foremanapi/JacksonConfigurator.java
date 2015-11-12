package fr.echoes.lab.foremanapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Provider
@Produces("application/*+json")
@Consumes("application/*+json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    public JacksonConfigurator() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        this.objectMapper.setSerializationInclusion(Include.NON_NULL);

    }
    @Override
	public ObjectMapper getContext(Class<?> objectType) {
        return this.objectMapper;
    }
}