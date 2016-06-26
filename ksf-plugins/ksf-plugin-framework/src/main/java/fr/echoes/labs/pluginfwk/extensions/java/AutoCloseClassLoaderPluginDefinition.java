package fr.echoes.labs.pluginfwk.extensions.java;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;
import fr.echoes.labs.pluginfwk.api.plugin.PluginDefinition;
import fr.echoes.labs.pluginfwk.api.plugin.PluginPropertiesAndDescription;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

public class AutoCloseClassLoaderPluginDefinition implements PluginDefinition {

	private final KSFURLClassLoader	ksfurlClassLoader;
	private final PluginDefinition	pluginDefinition;

	public AutoCloseClassLoaderPluginDefinition(final KSFURLClassLoader ksfurlClassLoader, final PluginDefinition pluginDefinition) {
		this.ksfurlClassLoader = ksfurlClassLoader;
		this.pluginDefinition = pluginDefinition;
	}

	@Override
	public void destroy() throws Exception {
		final ClassLoader backupCL = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.ksfurlClassLoader);
			this.pluginDefinition.destroy();

		} catch (final Throwable e) {
			throw e;
		} finally {
			Thread.currentThread().setContextClassLoader(backupCL);
		}
		this.ksfurlClassLoader.close();

	}

	@Override
	public String getDescription() {
		return this.pluginDefinition.getDescription();
	}

	@Override
	public IExtension[] getExtensions() {
		return this.pluginDefinition.getExtensions();
	}

	@Override
	public String getId() {
		return this.pluginDefinition.getId();
	}

	@Override
	public String getName() {
		return this.pluginDefinition.getName();
	}

	@Override
	public PluginPropertiesAndDescription getPluginProperties() {
		return this.pluginDefinition.getPluginProperties();
	}

	@Override
	public void init(final PluginPropertyStorage _pluginStorage) {
		final ClassLoader backupCL = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.ksfurlClassLoader);
			this.pluginDefinition.init(_pluginStorage);

		} catch (final Throwable e) {
			throw e;
		} finally {
			Thread.currentThread().setContextClassLoader(backupCL);
		}

	}

}
