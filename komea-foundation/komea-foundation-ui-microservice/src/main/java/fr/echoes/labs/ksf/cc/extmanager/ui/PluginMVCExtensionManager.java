package fr.echoes.labs.ksf.cc.extmanager.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.gui.IPluginGuiControllerExtension;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;
import fr.echoes.labs.pluginfwk.pluginloader.ExtensionManagerImpl;

@Service
public class PluginMVCExtensionManager implements IPluginMVCExtensionManager {

	private static final Logger									LOGGER					= LoggerFactory.getLogger(PluginMVCExtensionManager.class);

	private final Map<String, IPluginGuiControllerExtension>	pluginGuiControllers	= new HashMap<>();

	private final ExtensionManager								extensionManager;

	@Autowired
	public PluginMVCExtensionManager(final ExtensionManagerImpl extensionManager) {
		super();
		this.extensionManager = extensionManager;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extmanager.ui.IPluginMVCExtensionManager#getGuiController(java.lang.String)
	 */
	@Override
	public IPluginGuiControllerExtension getGuiController(final String _pluginGuiExtension) {
		return this.pluginGuiControllers.get(_pluginGuiExtension);

	}

	@PostConstruct
	public void init() {

		final List<IPluginGuiControllerExtension> extensions = this.extensionManager.findExtensions(IPluginGuiControllerExtension.class);
		if (extensions == null) {
			return;
		}
		LOGGER.info("Connecting Plugin MVC Controllers , found={}", extensions.size());
		for (final IPluginGuiControllerExtension extension : extensions) {
			this.pluginGuiControllers.put(extension.getUrlFragment(), extension);
		}
	}
}
