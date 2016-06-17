package fr.echoes.labs.ksf.cc.plugins.foreman.services;

import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanclient.ForemanClient;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForemanClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForemanClientFactory.class);

    private IForemanApi foremanAPI;

    private final ForemanConfigurationService configurationService;

    @Autowired
    public ForemanClientFactory(final ForemanConfigurationService configurationService) {
        this.configurationService = configurationService;
        this.initFormeanApi();
    }

    private void initFormeanApi() {
        try {
            this.getFormeanApi();
        } catch (Exception ex) {
            LOGGER.error("[foreman-plugin] error while initializing foreman client api : {}", ex);
        }
    }

    public IForemanApi createForemanClient() throws Exception {
        return this.getFormeanApi();
    }

    private synchronized IForemanApi getFormeanApi()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (this.foremanAPI == null) {
            try {
                // Manually inject the bean for breaking a circular dependency issue
                //currentUserService = applicationContext.getBean(ICurrentUserService.class);
                String url = this.configurationService.getForemanUrl();
                //String username = currentUserService.getCurrentUserLogin();
                //String password = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
                String username = this.configurationService.getForemanUsername();
                String password = this.configurationService.getForemanPassword();

                LOGGER.info("[foreman-plugin] init foreman client api for user {} on {}", username, url);

                this.foremanAPI = ForemanClient.createApi(url, username, password);
            } catch (Exception ex) {
                LOGGER.error("[foreman-plugin] error while initializing foreman client api : {}", ex);
                throw ex;
            }
        }
        return this.foremanAPI;
    }

}
