package fr.echoes.labs.foremanclient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnusedIpGetterTest {
	private UnusedIpGetter unusedIpGetter;

	@Before
    public void setUp() throws Exception {
    	this.unusedIpGetter = new UnusedIpGetter("");
    }

    @Test
    public void validInput() throws Exception {

    	matches("0.0.0.0", "{\"ip\":\"0.0.0.0\"}");
    	matches("255.255.255.255", "{\"ip\":\"255.255.255.255\"}");
    	matches("192.168.122.2", "{\"ip\":\"192.168.122.2\"}");
    	matches("192.168.122.22", "{\"ip\":\"192.168.122.22\"}");
    	matches("192.168.122.222", "{\"ip\":\"192.168.122.222\"}");

    }

    @Test
    public void invalidInput() throws Exception {
    	matches("", "");
    	matches("", null);
    	matches("", "{\"ip\":\"255.255.255.256\"}");
    	matches("", "{\"ip\":\"192.168.122.122.1\"}");
    	matches("", "{\"ip\":\"192.168.\"}");
    	matches("", "{\"ip\":\"192.168.122.a\"}");
    	matches("", "{\"ip\":\"192.168\"}");
    	matches("", "{}");
    }

	private void matches(String expected, String input) {
		final String result = this.unusedIpGetter.extractIp(input);
		Assert.assertEquals(expected, result);

	}
}
