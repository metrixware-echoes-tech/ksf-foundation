package fr.echoes.labs.ksf.extensions.api;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.echoes.labs.pluginfwk.api.extension.Extension;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

public class ExtensionResolver<T extends Extension> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionResolver.class);
	
	private final ApplicationContext applicationContext;
	private final ExtensionManager extensionManager;
	
	public ExtensionResolver(final ApplicationContext applicationContext, final ExtensionManager extensionManager) {
		this.applicationContext = applicationContext;
		this.extensionManager = extensionManager;
	}
	
	public List<T> findExtensions(final Class<T> extensionType) {
		
		List<T> extensions = Lists.newArrayList();
		
    	// retrieve all preloaded extensions
    	final Collection<T> beanExtensions = this.applicationContext.getBeansOfType(extensionType).values();
    	if (beanExtensions != null) {
    		extensions.addAll(beanExtensions);
    	}
    	
    	// retrieve all plugin extensions
    	final List<T> pluginExtensions = extensionManager.findExtensions(extensionType);   	
    	if (pluginExtensions != null) {
    		extensions.addAll(pluginExtensions);
    	}
    	
    	// remove duplicates
    	extensions = Lists.newArrayList(Sets.newHashSet(extensions));
    	
    	// sort extensions by @Order annotation
    	Collections.sort(extensions, ExtensionComparators.byOrderAnnotations());
    	
    	LOGGER.info("{} extensions of type {} found.", extensions.size(), extensionType);
    	for (final Extension extension : extensions) {
    		LOGGER.info("- {}", extension.getClass());
    	}
		
    	return extensions;
	}
	
}
