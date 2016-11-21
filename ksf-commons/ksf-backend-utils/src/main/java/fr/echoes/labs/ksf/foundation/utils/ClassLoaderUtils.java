package fr.echoes.labs.ksf.foundation.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

public final class ClassLoaderUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderUtils.class);
	
	public static String readResource(final ClassLoader classLoader, final String resourceName, final Charset charset) throws IOException {
		
		final ClassLoader initialClassLoader = Thread.currentThread().getContextClassLoader();
		
		URL url = null;
		String result = null;
		
		try {
			
			Thread.currentThread().setContextClassLoader(classLoader);
			
			url = Resources.getResource(resourceName);	
			result = Resources.toString(url, charset);
			
		} finally {
			Thread.currentThread().setContextClassLoader(initialClassLoader);
		}
		
		return result;
	}
	
}
