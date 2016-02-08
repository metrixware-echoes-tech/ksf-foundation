package fr.echoes.labs.ksf.cc.plugins.foreman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.inject.Guice;
import com.google.inject.Injector;

import fr.echoes.labs.ksf.extensions.api.IExtension;
import fr.echoes.labs.ksf.plugins.api.IPlugin;
import fr.echoes.labs.ksf.plugins.api.IPluginPropertiesDefinition;

@Component
public class ForemanPlugin implements IPlugin {
	
	private Injector injector;
	
	@Override
	public void close() throws IOException {
		injector = null;
	}
	
	@Override
	public String getDescription() {
		return "Foreman plugin for KSF";
	}
	
	/**
	 * Cette méthode sera utilisée dans la 1.1 , pour définir les extensions
	 * fournies par le plugin. Actuellement dans la 1.0 , les extensions sont
	 * scannées dans le classpath.
	 */
	@Override
	public List<IExtension> getExtensions() {
		final ForemanExtensions instance = new ForemanExtensions();
		injector.injectMembers(instance);
		return new ArrayList<>(instance.getExtensions());
	}
	
	@Override
	public String getId() {
		return "foreman-plugin";
	}
	
	@Override
	public IPluginPropertiesDefinition getPluginProperties() {
		return new PluginPropertiesDefinition();
	}
	
	@Override
	public String getSummary() {
		return "Foreman plugin bla bla bla";
	}
	
	@Override
	public void init() {
		// TODO:: Code qui sera utilisé dans la 1.1
		injector = Guice.createInjector(new ForemanExtensionGuiceModule());
		
	}
	
}
