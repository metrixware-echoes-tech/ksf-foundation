package com.tocea.corolla.backend.support.impl;

import com.google.common.collect.Lists;
import com.tocea.corolla.backend.support.api.FileStoreDto;
import com.tocea.corolla.backend.support.api.NetworkInterfaceDto;
import com.tocea.corolla.backend.support.api.PropertyDto;
import com.tocea.corolla.backend.support.api.SupportDto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SupportService implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportService.class.getName());

    private static final String ENCODING = "UTF-8";

    private static List<PropertyDto> convert(final Map<? extends Object, ? extends Object> map) {
        final List<PropertyDto> propertyDtos = Lists.newArrayList();
        for (final Object key : map.keySet()) {
            final Object value = map.get(key);
            propertyDtos.add(new PropertyDto(key.toString(), value == null ? "" : value.toString()));
        }
        Collections.sort(propertyDtos);
        return propertyDtos;
    }

    private static String getData(final List<? extends Object> list) {
        final StringBuilder sb = new StringBuilder(100);
        for (final Object o : list) {
            sb.append(o.toString()).append("\n");
        }
        return sb.toString();
    }

    private File tecmintFile;

    @PostConstruct
    public void init() {
        try {
            this.tecmintFile = File.createTempFile("tecmint_monitor-", ".sh");
            LOGGER.debug("Techmint script created at " + this.tecmintFile.getAbsolutePath());
            final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("tecmint_monitor.sh");
            final String scriptContent = IOUtils.toString(inputStream);
            FileUtils.write(this.tecmintFile, scriptContent);
            final ProcessBuilder processBuilder = new ProcessBuilder("chmod", "755", this.tecmintFile.getName());
            processBuilder.redirectErrorStream(true);
            processBuilder.directory(this.tecmintFile.getParentFile());
            final Process process = processBuilder.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.debug(line);
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        if (this.tecmintFile.exists()) {
            LOGGER.debug("Techmint script deleted at " + this.tecmintFile.getAbsolutePath());
        }
        FileUtils.deleteQuietly(this.tecmintFile);
    }

    public SupportDto buildSupportData() {
        final SupportDto supportDto = new SupportDto();
        supportDto.setSystemEnv(convert(System.getenv()));
        supportDto.setSystemFileStores(getSystemFileStores());
        supportDto.setSystemNetwork(getSystemNetwork());
        supportDto.setSystemProperties(convert(System.getProperties()));
        supportDto.setSystemRuntime(getSystemRuntime());
        supportDto.setSystemTime(getSystemTime());
        supportDto.setTechmintMonitor(getTecmintMonitor());
        return supportDto;
    }

    public File buildZipSupport() {
        final SupportDto supportDto = buildSupportData();
        ZipOutputStream zipOutputStream = null;
        try {
            final File zipFile = File.createTempFile("komea-support", ".zip");
            FileUtils.deleteQuietly(zipFile);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile), Charset.forName(ENCODING));
            addToZipFile("system-properties.txt", getData(supportDto.getSystemProperties()), zipOutputStream);
            addToZipFile("system-env.txt", getData(supportDto.getSystemEnv()), zipOutputStream);
            addToZipFile("system-runtime.txt", getData(supportDto.getSystemRuntime()), zipOutputStream);
            addToZipFile("system-time.txt", getData(supportDto.getSystemTime()), zipOutputStream);
            addToZipFile("system-network.txt", getData(supportDto.getSystemNetwork()), zipOutputStream);
            addToZipFile("system-filestores.txt", getData(supportDto.getSystemFileStores()), zipOutputStream);
            addToZipFile("techmint_monitor.txt", supportDto.getTechmintMonitor(), zipOutputStream);
            return zipFile;
        } catch (final Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(zipOutputStream);
        }
    }

    private List<FileStoreDto> getSystemFileStores() {
        final List<FileStoreDto> fileStoreDtos = Lists.newArrayList();
        try {
            for (final FileStore fileStore : FileSystems.getDefault().getFileStores()) {
                fileStoreDtos.add(new FileStoreDto(fileStore.name(), fileStore.type(), fileStore.getTotalSpace(),
                        fileStore.getUsableSpace(), fileStore.getUnallocatedSpace(), fileStore.isReadOnly()));
            }
        } catch (final Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return fileStoreDtos;
    }

    private List<NetworkInterfaceDto> getSystemNetwork() {
        final List<NetworkInterfaceDto> networkInterfaceDtos = Lists.newArrayList();
        try {
            final Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (final NetworkInterface netIf : Collections.list(nets)) {
                networkInterfaceDtos.add(new NetworkInterfaceDto(netIf));
            }
        } catch (final Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return networkInterfaceDtos;
    }

    private List<PropertyDto> getSystemRuntime() {
        final List<PropertyDto> properties = Lists.newArrayList();
        final Runtime runtime = Runtime.getRuntime();
        properties.add(new PropertyDto("availableProcessors", String.valueOf(runtime.availableProcessors())));
        properties.add(new PropertyDto("freeMemory", String.valueOf(runtime.freeMemory())));
        properties.add(new PropertyDto("totalMemory", String.valueOf(runtime.totalMemory())));
        properties.add(new PropertyDto("maxMemory", String.valueOf(runtime.maxMemory())));
        // properties.add(new PropertyDto("threads",
        // String.valueOf(ManagementFactory.getThreadMXBean().getThreadCount())));
        // properties.add(new PropertyDto("threads",
        // String.valueOf(Thread.activeCount())));
        return properties;
    }

    private List<PropertyDto> getSystemTime() {
        final List<PropertyDto> properties = Lists.newArrayList();
        properties.add(new PropertyDto("timezone", TimeZone.getDefault().getID()));
        properties.add(new PropertyDto("current", String.valueOf(System.nanoTime())));
        properties.add(new PropertyDto("iso8601", DatatypeConverter.printDateTime(new GregorianCalendar())));
        return properties;
    }

    private String getTecmintMonitor() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "Not available, only compatible with UNIX OS.";
        }
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", this.tecmintFile.getName());
            processBuilder.redirectErrorStream(true);
            processBuilder.directory(this.tecmintFile.getParentFile());
            final Process process = processBuilder.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line.replace("[32m", "").replace("[m", "").replace("(B", "").replace("[H", "")
                        .replace("[2J", "")).append('\n');
            }
            return stringBuilder.toString();
        } catch (final IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return "Error : " + ex.getMessage();
        }
    }

    private void addToZipFile(final String fileName, final String data, final ZipOutputStream zos) throws FileNotFoundException, IOException {
        final ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        zos.write(data.getBytes(ENCODING));
        zos.closeEntry();
    }

}
