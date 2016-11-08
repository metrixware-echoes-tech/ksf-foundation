package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.collect.Lists;

public abstract class CsvBackupService<T> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvBackupService.class);
	
	private static final String SLASH_REPLACEMENT = "\\-\\_\\-";
	
	protected static String encodeFileName(final String fileName) {
		
		return fileName.replaceAll("/", SLASH_REPLACEMENT);
	}
	
	protected static String decodeFileName(final String fileName) {
		
		return fileName.replaceAll(SLASH_REPLACEMENT, "/");
	}
	
	protected void write(final List<T> values, final String[] headers, final String folderPath, final String fileName) throws IOException {
		
		new File(folderPath).mkdirs();
		write(values, headers, folderPath + encodeFileName(fileName));
	}
	
	protected void write(final List<T> values, final String[] header, final String filePath) throws IOException {
		
		ICsvBeanWriter printer = null;
		
		try {
			printer = new CsvBeanWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);

			printer.writeHeader(header);
			
			for (T value : values) {
				printer.write(value, header);
			}
			
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (printer != null) {
				printer.close();
			}
		}
		
	}
	
	protected List<T> read(final String folderPath, final String fileName, final Class<T> beanClass) throws IOException {
		
		return read(folderPath + encodeFileName(fileName), beanClass);
	}
	
	protected List<T> read(final String filePath, final Class<T> beanClass) throws IOException {
		
		LOGGER.info("Reading file {}...", filePath);
		
		ICsvBeanReader beanReader = null;
		final List<T> results = Lists.newArrayList();
		
		T param;
		try {
			beanReader = new CsvBeanReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE);		
			final String[] header = beanReader.getHeader(true);
			param = beanReader.read(beanClass, header);
			while(param != null) {
				results.add(param);
				param = beanReader.read(beanClass, header);
			}
		} catch (IOException ex) {
			throw ex;
		}finally{
			if (beanReader != null) {
				beanReader.close();
			}
		}
		
		return results;
	}
	
}
