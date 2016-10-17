package fr.echoes.labs.ksf.foreman.api.utils;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KeepAsJsonDeserializerTest {

	private ObjectMapper mapper = new ObjectMapper();
	
	public static class Pojo {
		
		public Pojo() {
			// default constructor
		}
		
		@JsonDeserialize(using=KeepAsJsonDeserializer.class)
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
	}
	
	@Test
	public void testMappingString() throws IOException {
		
		final Pojo result = mapper.readValue("{ \"value\": \"something\" }", Pojo.class);
		
		Assert.assertEquals("something", result.getValue());
	}
	
	@Test
	public void testMappingEmptyString() throws IOException {
		
		final Pojo result = mapper.readValue("{ \"value\": \"\" }", Pojo.class);
		
		Assert.assertEquals("", result.getValue()); 
	}
	
	@Test
	public void testMappingJson() throws IOException {
		
		final Pojo result = mapper.readValue("{ \"value\": {\"innerValue\": \"something\"} }", Pojo.class);
		
		Assert.assertEquals("{\"innerValue\":\"something\"}", result.getValue());
	}
	
	@Test
	public void testMappingNull() throws IOException {
		
		final Pojo result = mapper.readValue("{\"value\":null}", Pojo.class);
		
		Assert.assertNull(result.getValue());
	}
	
}
