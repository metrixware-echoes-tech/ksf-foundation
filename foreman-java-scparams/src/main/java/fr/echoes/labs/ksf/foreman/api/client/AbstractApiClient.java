package fr.echoes.labs.ksf.foreman.api.client;

import java.io.IOException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServiceUnavailableException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public abstract class AbstractApiClient {

	protected final String protocol;
	protected final int port;
	protected final String host;
	protected final String username;
	protected final String password;
	
	protected final HttpClientContext context;
	
	public AbstractApiClient(final String protocol, final int port, final String host, final String username, final String password) {
		
		this.protocol = protocol;
		this.port = port;
		this.host = host;
		this.username = username;
		this.password = password;
		this.context = buildContext(protocol, port, host, username, password);
	}
	
	private static HttpClientContext buildContext(final String protocol, final int port, final String host, final String username, final String password) {
		
		HttpHost targetHost = new HttpHost(host, port, protocol);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, 
		  new UsernamePasswordCredentials(username, password));
		 
		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());
		 
		// Add AuthCache to the execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
		
		return context;
	}
	
	protected static String extractBody(final HttpResponse response) throws UnsupportedOperationException, IOException {
		
		return IOUtils.toString(response.getEntity().getContent());
	}
	
	protected String get(final String url, final String contentType) throws IOException {
		
		final HttpGet method = new HttpGet(url);
		method.addHeader("Content-Type", contentType);
		method.addHeader("Accept", contentType);

		return this.executeRequest(method);
	}
	
	protected void put(final String url, final String contentType, final String content) throws IOException {
		
		final HttpPut method = new HttpPut(url);
		method.addHeader("Content-Type", contentType);
		method.addHeader("Accept", contentType);
		
		StringEntity input = new StringEntity(content);
		input.setContentType(contentType);
		method.setEntity(input);
		
		this.executeRequest(method);
	}
	
	protected void post(final String url, final String contentType, final String content) throws IOException {
		
		final HttpPost method = new HttpPost(url);
		method.addHeader("Content-Type", contentType);
		method.addHeader("Accept", contentType);
		
		StringEntity input = new StringEntity(content);
		input.setContentType(contentType);
		method.setEntity(input);
		
		this.executeRequest(method);
	}
	
	protected String executeRequest(final HttpUriRequest request) throws IOException {
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		
		try {
			client = HttpClientBuilder.create().build();
			response = client.execute(request, this.context);
			handleError(response);
			return extractBody(response);
		} catch (IOException | ClientErrorException ex) {
			throw ex;
		} finally {
			if (client != null) client.close();
			if (response != null) response.close();
		}	
	}
	
	protected void handleError(final HttpResponse response) throws IOException {
		
		int statusCode = response.getStatusLine().getStatusCode();
		
		switch(statusCode) {
			case HttpStatus.SC_UNAUTHORIZED:
			case HttpStatus.SC_FORBIDDEN:
				throw new ForbiddenException(extractBody(response));
			case HttpStatus.SC_NOT_FOUND:
				throw new NotFoundException(extractBody(response));
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				throw new InternalServerErrorException(extractBody(response));
			case HttpStatus.SC_GATEWAY_TIMEOUT:
				throw new ConnectTimeoutException(extractBody(response));
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				throw new ServiceUnavailableException(extractBody(response));
			case HttpStatus.SC_REQUEST_TIMEOUT:
				throw new ConnectTimeoutException(extractBody(response));
			case HttpStatus.SC_BAD_REQUEST:
			case HttpStatus.SC_METHOD_NOT_ALLOWED:
			case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
			case HttpStatus.SC_BAD_GATEWAY:
				throw new BadRequestException(extractBody(response));
		}
		
	}
	
}
