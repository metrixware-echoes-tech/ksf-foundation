package fr.echoes.labs.pluginfwk.extensions.java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KSFURLClassLoader extends URLClassLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(KSFURLClassLoader.class);

	public static KSFURLClassLoader newPluginClassLoader(final ClassLoader _parentClassLoader, final Collection<File> jarFiles) {

		final URL[] jarFileURLS = new URL[jarFiles.size()];
		int i = 0;
		LOGGER.debug("Creates classloader with {} extra libraries", jarFiles.size());
		for (final File jarFile : jarFiles) {
			try {
				LOGGER.debug("> Library {}", jarFile);
				jarFileURLS[i++] = jarFile.toURI().toURL();
			} catch (final Throwable e) {
				LOGGER.error("Could not load the extension library {}", jarFile, e);
			}
		}

		return new KSFURLClassLoader(_parentClassLoader, jarFileURLS);
	}

	/**
	 * Instantiates a new KSFURL class loader.
	 *
	 * @param _parentClassLoader
	 *            the _parent class loader
	 * @param urls
	 * @param jarFiles
	 *            the jar files
	 */
	public KSFURLClassLoader(final ClassLoader _parentClassLoader, final URL[] urls) {
		super(urls, _parentClassLoader);

	}

	@Override
	public void close() throws IOException {
		super.close();
	}

}
