package fr.echoes.labs.ksf.cc.ui.views

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
public class ViewRouter extends WebMvcConfigurerAdapter {
	
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
		
        registry.addViewController("/login").setViewName("login")
        registry.addViewController("/logout").setViewName("logout")
        registry.addViewController("/ui/404.html").setViewName("404")
        registry.addViewController("/ui/401.html").setViewName("401")
        registry.addViewController("/ui/500.html").setViewName("500")
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}
	
}
