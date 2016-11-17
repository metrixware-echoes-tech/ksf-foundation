package fr.echoes.labs.ksf.plugins.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafTemplateEngine {

	private final TemplateEngine templateEngine;
	
	public ThymeleafTemplateEngine() {
		
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("XHTML");
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		
	}
	
	/**
	 * Process a Thymeleaf template using an external class loader
	 * @param templateName
	 * @param context
	 * @param classLoader
	 * @return
	 */
	public String process(final String templateName, final IContext context, final ClassLoader classLoader) {
	
		String result;
		final ClassLoader initialClassLoader = Thread.currentThread().getContextClassLoader();
		
		try {
			
			Thread.currentThread().setContextClassLoader(classLoader);
			
			result = this.templateEngine.process(templateName, context);
			
		}finally{
			Thread.currentThread().setContextClassLoader(initialClassLoader);
		}
	
		return result;
	}
	
}
