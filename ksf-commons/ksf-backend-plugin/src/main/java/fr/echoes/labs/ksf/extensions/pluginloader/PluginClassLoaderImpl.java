package fr.echoes.labs.ksf.extensions.pluginloader;

import org.springframework.stereotype.Service;

@Service
public class PluginClassLoaderImpl {

	public void closeClassLoader() {

	}

	/**
	 * Gets the class loader.
	 *
	 * @return the class loader
	 */
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public void reloadClassLoader() {

	}

}
