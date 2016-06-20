package fr.echoes.labs.pluginfwk.pluginloader;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KSFURLClassLoader extends ClassLoader implements Closeable {

	private static final Logger		LOGGER	= LoggerFactory.getLogger(KSFURLClassLoader.class);

	private final URLClassLoader	urlClassLoader;

	/**
	 * Instantiates a new KSFURL class loader.
	 *
	 * @param _parentClassLoader
	 *            the _parent class loader
	 * @param jarFiles
	 *            the jar files
	 */
	public KSFURLClassLoader(final ClassLoader _parentClassLoader, final Collection<File> jarFiles) {
		final URL[] jarFileURLS = new URL[jarFiles.size()];
		final int i = 0;
		LOGGER.info("Found {} extra libraries", jarFiles.size());
		for (final File jarFile : jarFiles) {
			try {
				LOGGER.info("> Library {}", jarFile);
				jarFileURLS[i] = jarFile.toURI().toURL();
			} catch (final Exception e) {
				LOGGER.error("Could not load the extension library {}", jarFile, e);
			}
		}
		this.urlClassLoader = new URLClassLoader(jarFileURLS, _parentClassLoader);

	}

	@Override
	public void close() throws IOException {
		this.urlClassLoader.close();
	}

}
