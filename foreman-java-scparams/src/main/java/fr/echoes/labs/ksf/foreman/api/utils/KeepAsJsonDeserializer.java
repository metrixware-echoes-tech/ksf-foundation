package fr.echoes.labs.ksf.foreman.api.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class KeepAsJsonDeserializer extends JsonDeserializer<String>{

	@Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        final TreeNode tree = jp.getCodec().readTree(jp);
        
        return cleanUp(tree.toString());
    }
	
	public static String cleanUp(final String value) {
		
		if (value != null) {
			if (value.length() > 1 && value.charAt(0) == '"' && value.charAt(value.length()-1) == '"') {
				return value.substring(1, value.length()-1);
			}
			return value;
		}
		
		return null;
	}
	
}
