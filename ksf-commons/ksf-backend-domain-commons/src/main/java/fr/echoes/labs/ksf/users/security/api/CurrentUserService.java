package fr.echoes.labs.ksf.users.security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final ApplicationContext applicationContext;
    private ICurrentUserService currentUserService;

    @Autowired
    public CurrentUserService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private synchronized ICurrentUserService getCurrentUserService() {
        if (this.currentUserService == null) {
            this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
        }
        return this.currentUserService;
    }

    public String getCurrentUserLogin() {
        return this.getCurrentUserService().getCurrentUserLogin();
    }

}
