package fr.echoes.labs.ksf.cc.extensions.gui;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.WebContext;

import com.google.common.collect.Lists;

@Service
public class KomeaFoundationContext {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KomeaFoundationContext.class);

	@Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment env;
    
    public void completeProperties(final Object bean) {
    	
    	for (final Field field : bean.getClass().getDeclaredFields()) {
    		try {
				final Object currentValue = (Object) PropertyUtils.getProperty(bean, field.getName());
				if (currentValue == null) {
					LOGGER.debug("Unset configuration property found: {}", field.getName());
					final String annotation = getValueAnnotation(field);
					if (annotation != null) {
						LOGGER.debug("Looking for entry {} in properties file...", annotation);
						Object propValue = getProperty(annotation, field.getType());
						if (propValue != null) {
							LOGGER.debug("Setting property {} with value {}.", field.getName(), propValue);
							PropertyUtils.setProperty(bean, field.getName(), propValue);
						}
					}
				}
			} catch (final Exception ex) {
				LOGGER.error("Cannot handle property "+field.getName(), ex);
			}
    	}
    }
    
    private Object getProperty(final String key, final Class<?> type) {
    	
    	final String propValue = env.getProperty(key);
    	
		if (propValue != null) {
			if (type.equals(List.class) && propValue instanceof String) {
				return Lists.newArrayList(propValue.split(","));
			}
			return ConvertUtils.convert(propValue, type);
		}
		
		return null;
    }
    
    private static String getValueAnnotation(final Field field) {
    	final Value value = field.getAnnotation(Value.class);
		if (value != null && value.value() != null) {
			return extractPropertyName(value.value());
		}
		return null;
    }
    
    private static String extractPropertyName(final String value) {
    	if (value != null) {
    		return value.replaceAll("\\$\\{", "")
					.replaceAll("\\}", "")
					.replaceAll(":", "");
    	}
    	return null;
    }
    
    public WebContext newThymeleafWebContext() {
    	return new WebContext(this.request, this.response, this.servletContext);
    }
    
    public String getContextPath() {
    	return this.request.getContextPath();
    }
    
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
}
