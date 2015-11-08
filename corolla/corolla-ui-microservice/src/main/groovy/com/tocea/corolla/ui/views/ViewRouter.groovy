package com.tocea.corolla.ui.views

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
public class ViewRouter extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		
		registry.addViewController("/login").setViewName("login")
                registry.addViewController("/logout").setViewName("logout")
		registry.addRedirectViewController "/home", "/ui/home"
	}
}
