package fr.echoes.labs.pluginfwk.extensions.java;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class ScanUtils {

	/**
	 * Scan a folder for files with the matching extensions.
	 *
	 * @param folder
	 *            the folder
	 * @param _extensions
	 *            the extensions
	 * @return the collection
	 */
	public static Collection<File> scanForExtensions(final File folder, final String... _extensions) {
		return FileUtils.listFiles(folder, _extensions, true);
	}

	/**
	 * Scan a folder for jars.
	 *
	 * @param folder
	 *            the folder
	 * @return the collection
	 */
	public static Collection<File> scanForJars(final File folder) {
		return scanForExtensions(folder, "jar");
	}
}
