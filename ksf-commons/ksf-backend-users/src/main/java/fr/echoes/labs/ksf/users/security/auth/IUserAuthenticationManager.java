package fr.echoes.labs.ksf.users.security.auth;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public interface IUserAuthenticationManager {

    public abstract void initializeKsfAuth(AuthenticationManagerBuilder auth)
            throws Exception;

}
