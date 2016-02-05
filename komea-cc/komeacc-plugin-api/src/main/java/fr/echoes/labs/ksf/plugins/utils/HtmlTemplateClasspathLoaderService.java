package fr.echoes.labs.ksf.plugins.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlTemplateClasspathLoaderService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(HtmlTemplateClasspathLoaderService.class);
	
	/**
	 * Returns the html content from a classpath.
	 *
	 * @param _classLoader
	 * @param _htmlResource
	 *            the html resource
	 * @return the html content.
	 */
	public String getHtmlContent(final ClassLoader _classLoader, final String _htmlResource) {
		LOGGER.debug("Trying to retrieve the template {}", _htmlResource);
		try (InputStream resourceAsStream = _classLoader.getResourceAsStream(_htmlResource);) {
			return IOUtils.toString(resourceAsStream);
		} catch (final Exception e) {
			LOGGER.error("Coudl not retrieve the template {}", _htmlResource);
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final PrintStream printStream = new PrintStream(byteArrayOutputStream);
			printStream.print("<div><code>");
			e.printStackTrace(printStream);
			printStream.print("</code></div>");
			return new String(byteArrayOutputStream.toByteArray());
		}
	}
}
