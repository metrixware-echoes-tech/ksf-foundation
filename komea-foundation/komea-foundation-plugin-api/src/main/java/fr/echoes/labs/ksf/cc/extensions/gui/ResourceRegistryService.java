package fr.echoes.labs.ksf.cc.extensions.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResourceRegistryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceRegistryService.class);
	
	private static final String EXTERNAL_RESOURCE_FOLDER = "public/";

	public void registerResource(final ClassLoader classLoader, final String resource) {
		registerResource(classLoader, "", resource);
	}
	
	public void registerResources(final ClassLoader classLoader, final List<String> resources) {
		registerResources(classLoader, "", resources);
	}
	
	public void registerResource(final ClassLoader classLoader, final String destinationFolder, final String resource) {
		exportResource(classLoader, EXTERNAL_RESOURCE_FOLDER + destinationFolder, resource);
	}
	
	public void registerResources(final ClassLoader classLoader, final String destinationFolder, final List<String> resources) {
		for (final String resource : resources) {
			registerResource(classLoader, destinationFolder, resource);
		}
	}
	
	private static void exportResource(final ClassLoader classLoader, final String destinationFolder, final String resourceFile) {
		
		final URL resourceURL = classLoader.getResource(resourceFile);
		final InputStream inputStream = classLoader.getResourceAsStream(resourceFile);
		OutputStream outputStream = null;
		
		if (resourceURL != null) {
			try {
				final File destination = new File(destinationFolder);
				if (!destination.exists()) {
					LOGGER.info("Creating external resources folder {}...", destinationFolder);
					destination.mkdirs();
				}
				LOGGER.info("Exporting file {} into {}...", resourceFile, destination.getAbsolutePath());
				outputStream = new FileOutputStream(new File(destination, getFilename(resourceFile)));
				final byte[] buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				outputStream.write(buffer);
				
			} catch (final IOException ex) {
				LOGGER.error("Cannot export resource file " + resourceFile, ex);
			} finally {
				try {
					if (inputStream != null) inputStream.close();
					if (outputStream != null) outputStream.close();
				} catch (final IOException ex) {
					LOGGER.error("Could not close buffer.", ex);
				}
			}
		}else{
			LOGGER.error("The file {} cannot be found in the resource folder.", resourceFile);
		}
		
	}
	
	private static String getFilename(final String filePath) {
		
		if (filePath != null) {
			final String[] parts = filePath.split("/");
			return parts[parts.length-1];
		}
		
		return null;
	}
	
}
