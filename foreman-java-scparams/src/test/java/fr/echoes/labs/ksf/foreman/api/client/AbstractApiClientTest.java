package fr.echoes.labs.ksf.foreman.api.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServiceUnavailableException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractApiClientTest {

	private AbstractApiClient client;
	
	private class TestClient extends AbstractApiClient {
		
		public TestClient(String protocol, int port, String host, String username, String password) {
			super(protocol, port, host, username, password);
		}

	}
	
	@Before
	public void setup() {
		
		this.client = new TestClient("http", 8080, "localhost", "", "");
		
	}
	
	private static HttpResponse buildResponseMock(final int statusCode) throws IllegalStateException, IOException {
		
		final InputStream stream = new ByteArrayInputStream("ResponseContent".getBytes(StandardCharsets.UTF_8));
		final StatusLine status = Mockito.mock(StatusLine.class);
		final HttpEntity entity = Mockito.mock(HttpEntity.class);
		final HttpResponse response = Mockito.mock(HttpResponse.class);
		
		Mockito.when(entity.getContent()).thenReturn(stream);
		Mockito.when(response.getEntity()).thenReturn(entity);
		Mockito.when(response.getStatusLine()).thenReturn(status);
		Mockito.when(status.getStatusCode()).thenReturn(statusCode);
		
		return response;
	}
	
	@Test(expected = BadRequestException.class)
	public void testBadRequest() throws IOException {
		
		this.client.handleError(buildResponseMock(400));
	}
	
	@Test(expected = ForbiddenException.class)
	public void testUnauthorized() throws IOException {
		
		this.client.handleError(buildResponseMock(401));
	}
	
	@Test(expected = ForbiddenException.class)
	public void testForbidden() throws IOException {
		
		this.client.handleError(buildResponseMock(403));
	}
	
	@Test(expected = NotFoundException.class)
	public void testNotFound() throws IOException {
		
		this.client.handleError(buildResponseMock(404));
	}
	
	@Test(expected = BadRequestException.class)
	public void testMethodNotAllowed() throws IOException {
		
		this.client.handleError(buildResponseMock(405));
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void testInternalServerError() throws IOException {
		
		this.client.handleError(buildResponseMock(500));
	}
	
	@Test(expected = ConnectTimeoutException.class)
	public void testGatewayTimeout() throws IOException {
		
		this.client.handleError(buildResponseMock(504));
	}
	
	@Test(expected = ConnectTimeoutException.class)
	public void testTimeout() throws IOException {
		
		this.client.handleError(buildResponseMock(408));
	}
	
	@Test(expected = ServiceUnavailableException.class)
	public void testServiceUnavailable() throws IOException {
		
		this.client.handleError(buildResponseMock(503));
	}
	
	@Test(expected = BadRequestException.class)
	public void testBadGateway() throws IOException {
		
		this.client.handleError(buildResponseMock(502));
	}
	
	@Test(expected = BadRequestException.class)
	public void testUnsupportedMediaType() throws IOException {
		
		this.client.handleError(buildResponseMock(415));
	}
	
}
