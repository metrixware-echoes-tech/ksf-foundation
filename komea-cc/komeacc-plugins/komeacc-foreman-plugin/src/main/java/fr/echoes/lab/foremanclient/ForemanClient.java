package fr.echoes.lab.foremanclient;


import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.lab.foremanapi.IForemanApi;

public class ForemanClient {

	final static Logger LOGGER = LoggerFactory.getLogger(ForemanClient.class);


	/**
	 * Private constructor to prevent instantiation.
	 */
	private ForemanClient() {
		// Do nothing
	}

	public static IForemanApi createApi(String url, String username, String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		URL parsedUrl;
		try {
			parsedUrl = new URL(url);
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
		int port = parsedUrl.getPort();
		if(port == -1){
			LOGGER.debug("Port was omitted, using a default port for given protocol");
			port = parsedUrl.getDefaultPort();
		}

		LOGGER.info("Creating a new proxy - host: {}, port: {}, protocol: {}, user: {}",
				parsedUrl.getHost(),port,parsedUrl.getProtocol(),username);

		// Configure HttpClient to authenticate preemptively by prepopulating the authentication data cache.
		final AuthCache authCache = new BasicAuthCache();

		// Generate BASIC scheme object and add it to the local auth cache
		final AuthScheme basicAuth = new BasicScheme();
		authCache.put(new HttpHost(parsedUrl.getHost(),port,parsedUrl.getProtocol()), basicAuth);

		// Add AuthCache to the execution context
		final BasicHttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);


		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		httpClientBuilder.setSSLSocketFactory(createTrustfulSsLConnectionSocketFactory());
		httpClientBuilder.setDefaultCredentialsProvider(createCredentialsProvider(username, password));

		// or SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
		final X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
		    @Override
			public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		        return true;
		    }
		}).build();

		httpClientBuilder.setSslcontext( sslContext);



		final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
		final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
		        .register("http", PlainConnectionSocketFactory.getSocketFactory())
		        .register("https", sslSocketFactory)
		        .build();

		// Create client executor and proxy
		final HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		httpClientBuilder.setConnectionManager(cm);

		final HttpClient httpClient = httpClientBuilder.build();

		final ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine( httpClient, localContext);
		final ResteasyClient client = new ResteasyClientBuilder()
										.httpEngine(engine)
										.establishConnectionTimeout(60, TimeUnit.SECONDS)
										.build();

		client.register(ClientErrorResponseFilter.class);
		client.register(JacksonJaxbJsonProvider.class);
//		client.register(JacksonConfigurator.class);
		client.register(AddVersionHeaderRequestFilter.class);

		  final ObjectMapper objectMapper=new ObjectMapper();

		  objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		  final JacksonJaxbJsonProvider jacksonProvider=new JacksonJaxbJsonProvider();
//		  jacksonProvider.setMapper(m);
		  jacksonProvider.setMapper(objectMapper);
		  client.register(jacksonProvider);

        final ResteasyWebTarget target = client.target(url);

        return target.proxy(IForemanApi.class);
	}

	private static CredentialsProvider createCredentialsProvider(String username, String password) {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
		return credentialsProvider;
	}

	private static SSLConnectionSocketFactory createTrustfulSsLConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
	    final SSLContextBuilder builder = new SSLContextBuilder();
	    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	    final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
	    return sslsf;
	}

}
