package com.tocea.corolla

import javax.servlet.DispatcherType
import javax.servlet.Filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
import org.springframework.boot.context.embedded.ErrorPage
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer

/**
 * This class is the replacement of the web.xml. It registers the wicket filter
 * in the spring aware configuration style.
 *
 * @author Stefan Kloe
 *
 */
@Configuration
class WebInitializer {

	def static final String	PARAM_APP_BEAN	= "applicationBean"

	def static final Logger	LOGGER			= LoggerFactory.getLogger(WebInitializer.class)


	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
					@Override
					public void customize(ConfigurableEmbeddedServletContainer container) {

						ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/ui/401.html")
						ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/ui/404.html")
						ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/ui/500.html")

						container.addErrorPages(error401Page, error404Page, error500Page)
					}
				}
	}


	@Bean
	public FilterRegistrationBean securityFilterChain(
			@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) final Filter securityFilter) {
		LOGGER.info ">---Web.xml Spring Security configuration.."
		final FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter)
		registration.setOrder(0)
		registration
				.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
		LOGGER.info "<----Web.xml Spring Security configuration.."
		return registration
	}
}
