package fr.echoes.labs.ksf.cc.extmanager.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.extensions.gui.IPluginGuiControllerExtension;
import fr.echoes.labs.ksf.extensions.annotations.ExtensionPoint;

@ExtensionPoint
public class PluginMVCExtensionManager implements IPluginMVCExtensionManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginMVCExtensionManager.class);
	
	private final Map<String, IPluginGuiControllerExtension> pluginGuiControllers = new HashMap<>();
	
	@Autowired(required = false)
	private IPluginGuiControllerExtension[] extensions;
	
	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extmanager.ui.IPluginMVCExtensionManager#getGuiController(java.lang.String)
	 */
	@Override
	public IPluginGuiControllerExtension getGuiController(final String _pluginGuiExtension) {
		return pluginGuiControllers.get(_pluginGuiExtension);
		
	}
	
	@PostConstruct
	public void init() {
		if (extensions == null) {
			return;
		}
		LOGGER.info("Connecting Plugin MVC Controllers , found={}", extensions.length);
		for (final IPluginGuiControllerExtension extension : extensions) {
			pluginGuiControllers.put(extension.getUrlFragment(), extension);
		}
	}
}
