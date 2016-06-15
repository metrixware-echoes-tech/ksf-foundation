package fr.echoes.labs.ksf.cc.plugins.nexus.extensions;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.jboss.resteasy.client.jaxrs.engines.PassthroughTrustManager;
import org.junit.Ignore;
import org.junit.Test;
import org.testng.Assert;

public class NexusProjectLifeCycleExtensionTest {

    @Ignore
    @Test
    public void testMain() throws KeyManagementException, NoSuchAlgorithmException {
        final String username = "ksfuser";
        final String password = "490ebc83a2b4dc242a957551e082a1a6024259ef9676058e72fc8851ac1bf22b";

        // Allow all ssl certificates
        final SSLContext sc = SSLContext.getInstance("TLSv1");
        final TrustManager[] trustAllCerts = {new PassthroughTrustManager()};
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        final HostnameVerifier allHostsValid = new AllowAllHostnameVerifier();

        final Client client = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid)
                .register(new Authenticator(username, password)).build();

        final WebTarget target = client.target("https://ksf-demo.metrixware.local/nexus").path("service/local/repositories");
        final Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_XML);

        final RepositoryData data = new RepositoryData()
                .setId("my-releases-xml8")
                .setName("MyReleasesXml 8")
                .setProvider(RepositoryData.PROVIDER_MAVEN2)
                .setProviderRole(RepositoryData.PROVIDER_ROLE_REPOSITORY)
                .setFormat(RepositoryData.FORMAT_MAVEN2)
                .setRepoType(RepositoryData.REPO_TYPE_HOSTED)
                .setExposed(true)
                .setWritePolicy(RepositoryData.WRITE_POLICY_ALLOW_WRITE_ONCE)
                .setBrowseable(true)
                .setIndexable(true)
                .setNotFoundCacheTTL(1440)
                .setRepoPolicy(RepositoryData.REPO_POLICY_RELEASE)
                .setDownloadRemoteIndexes(false);

        final Repository repository = new Repository().setData(data);
        final Response response = invocationBuilder.post(Entity.entity(repository, MediaType.APPLICATION_XML));
        Assert.assertEquals(response.getStatus(), HttpStatus.SC_CREATED,
                "Expected response status is CREATED : Code " + HttpStatus.SC_CREATED);
    }
}
