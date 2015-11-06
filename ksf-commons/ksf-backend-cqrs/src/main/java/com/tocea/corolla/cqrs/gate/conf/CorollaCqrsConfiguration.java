package com.tocea.corolla.cqrs.gate.conf;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration of the server.
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "corolla.settings.cqrs")
public class CorollaCqrsConfiguration {

    private boolean loggingEnabled = true;
    private boolean profilingEnabled = false;
    private int historyCapacity = 3;
    private boolean forceSync = false;
    private final int corePoolSize = 2;
    private int maxPoolSize = 10;
    private int queueCapacity = 25;
    private boolean tracingEnabled = false;
    private File traceFile = new File("command.trace");
    private final int keepAliveSeconds = 60;
    private boolean asyncEventQueries = false;

    public int getCorePoolSize() {

        return corePoolSize;
    }

    public int getHistoryCapacity() {
        return historyCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public File getTraceFile() {
        return traceFile;
    }

    public boolean isForceSync() {
        return forceSync;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public boolean isProfilingEnabled() {
        return profilingEnabled;
    }

    public boolean isTracingEnabled() {
        return tracingEnabled;
    }

    public void setForceSync(final boolean _forceSync) {
        forceSync = _forceSync;
    }

    public void setHistoryCapacity(final int _historyCapacity) {
        historyCapacity = _historyCapacity;
    }

    public void setLoggingEnabled(final boolean _loggingEnabled) {
        loggingEnabled = _loggingEnabled;
    }

    public void setMaxPoolSize(final int _maxPoolSize) {
        maxPoolSize = _maxPoolSize;
    }

    public void setProfilingEnabled(final boolean _profilingEnabled) {
        profilingEnabled = _profilingEnabled;
    }

    public void setQueueCapacity(final int _queueCapacity) {
        queueCapacity = _queueCapacity;
    }

    public void setTraceFile(final File _logFile) {
        traceFile = _logFile;
    }

    public void setTracingEnabled(final boolean _logCommands) {
        tracingEnabled = _logCommands;
    }

    @Override
    public String toString() {
        return "CorollaCqrsConfiguration [loggingEnabled=" + loggingEnabled + ", profilingEnabled=" + profilingEnabled
                + ", historyCapacity=" + historyCapacity + ", forceSync=" + forceSync + ", corePoolSize=" + corePoolSize
                + ", maxPoolSize=" + maxPoolSize + ", queueCapacity=" + queueCapacity + ", logCommands="
                + tracingEnabled + ", logFile=" + traceFile + "]";
    }

    /**
     * @return the asyncEventQueries
     */
    public boolean isAsyncEventQueries() {
        return asyncEventQueries;
    }

    /**
     * @param asyncEventQueries the asyncEventQueries to set
     */
    public void setAsyncEventQueries(boolean asyncEventQueries) {
        this.asyncEventQueries = asyncEventQueries;
    }

}
