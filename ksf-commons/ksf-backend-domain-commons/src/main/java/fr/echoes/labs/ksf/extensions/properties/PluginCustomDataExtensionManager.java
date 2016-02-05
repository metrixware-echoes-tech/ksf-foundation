package fr.echoes.lab.ksf.extensions.properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This interface allows a plugin to store custom data into the backend.
 *
 * @author sleroy
 *
 */
@Service
public class PluginCustomDataExtensionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginCustomDataExtensionManager.class);

	@Autowired(required=false)
	public IPluginCustomDataExtension[] extensions;

	@Autowired
	private ICustomDataDAOFactory customDaofactory;

	@PostConstruct
	public void initDAO() {

		LOGGER.info("Initializing the Custom daos : extensions found : {}", extensions == null ? 0 : extensions.length);
		if (extensions == null) {
			return ;
		}
		for (final IPluginCustomDataExtension extension : extensions) {
			extension.declareCustomDataDAO(customDaofactory);
		}
	}

}