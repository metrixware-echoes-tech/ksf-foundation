package com.tocea.corolla.cqrs.gate.conf;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration of the CQRS Engine.
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "corolla.settings.cqrs")
public class CqrsConfiguration {

	private boolean	loggingEnabled		= true;
	private boolean	profilingEnabled	= false;
	private int		corePoolSize		= 4;
	private int		maxPoolSize			= -1;
	private int		queueCapacity		= -1;
	private boolean	tracingEnabled		= false;
	private File	traceFile			= new File("command.trace");
	private int		keepAliveSeconds	= 60;
	private boolean	asyncEventQueries	= false;
	private int		threadPriority		= 5;

	/**
	 * Gets the core pool size.
	 *
	 * @return the core pool size
	 */
	public int getCorePoolSize() {

		return corePoolSize;
	}

	/**
	 * Gets the keep alive seconds.
	 *
	 * @return the keep alive seconds
	 */
	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	/**
	 * Gets the max pool size.
	 *
	 * @return the max pool size
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * Gets the queue capacity.
	 *
	 * @return the queue capacity
	 */
	public int getQueueCapacity() {
		return queueCapacity;
	}

	/**
	 * Gets the thread priority.
	 *
	 * @return the thread priority
	 */
	public int getThreadPriority() {
		return threadPriority;
	}

	/**
	 * Gets the trace file.
	 *
	 * @return the trace file
	 */
	public File getTraceFile() {
		return traceFile;
	}

	/**
	 * Checks if is async event queries.
	 *
	 * @return the asyncEventQueries
	 */
	public boolean isAsyncEventQueries() {
		return asyncEventQueries;
	}

	/**
	 * Checks if is logging enabled.
	 *
	 * @return true, if is logging enabled
	 */
	public boolean isLoggingEnabled() {
		return loggingEnabled;
	}

	/**
	 * Checks if is profiling enabled.
	 *
	 * @return true, if is profiling enabled
	 */
	public boolean isProfilingEnabled() {
		return profilingEnabled;
	}

	/**
	 * Checks if is tracing enabled.
	 *
	 * @return true, if is tracing enabled
	 */
	public boolean isTracingEnabled() {
		return tracingEnabled;
	}

	/**
	 * Sets the async event queries.
	 *
	 * @param asyncEventQueries
	 *            the asyncEventQueries to set
	 */
	public void setAsyncEventQueries(final boolean asyncEventQueries) {
		this.asyncEventQueries = asyncEventQueries;
	}

	/**
	 * Sets the core pool size.
	 *
	 * @param _corePoolSize
	 *            the new core pool size
	 */
	public void setCorePoolSize(final int _corePoolSize) {
		corePoolSize = _corePoolSize;
	}

	/**
	 * Sets the keep alive seconds.
	 *
	 * @param _keepAliveSeconds
	 *            the new keep alive seconds
	 */
	public void setKeepAliveSeconds(final int _keepAliveSeconds) {
		keepAliveSeconds = _keepAliveSeconds;
	}

	/**
	 * Sets the logging enabled.
	 *
	 * @param _loggingEnabled the new logging enabled
	 */
	public void setLoggingEnabled(final boolean _loggingEnabled) {
		loggingEnabled = _loggingEnabled;
	}

	/**
	 * Sets the max pool size.
	 *
	 * @param _maxPoolSize the new max pool size
	 */
	public void setMaxPoolSize(final int _maxPoolSize) {
		maxPoolSize = _maxPoolSize;
	}

	/**
	 * Sets the profiling enabled.
	 *
	 * @param _profilingEnabled the new profiling enabled
	 */
	public void setProfilingEnabled(final boolean _profilingEnabled) {
		profilingEnabled = _profilingEnabled;
	}

	/**
	 * Sets the queue capacity.
	 *
	 * @param _queueCapacity the new queue capacity
	 */
	public void setQueueCapacity(final int _queueCapacity) {
		queueCapacity = _queueCapacity;
	}

	/**
	 * Sets the thread priority.
	 *
	 * @param _threadPriority the new thread priority
	 */
	public void setThreadPriority(final int _threadPriority) {
		threadPriority = _threadPriority;
	}

	/**
	 * Sets the trace file.
	 *
	 * @param _logFile the new trace file
	 */
	public void setTraceFile(final File _logFile) {
		traceFile = _logFile;
	}

	/**
	 * Sets the tracing enabled.
	 *
	 * @param _logCommands the new tracing enabled
	 */
	public void setTracingEnabled(final boolean _logCommands) {
		tracingEnabled = _logCommands;
	}

	@Override
	public String toString() {
		return "CqrsConfiguration [loggingEnabled=" + loggingEnabled + ", profilingEnabled=" + profilingEnabled
				+ ", corePoolSize=" + corePoolSize + ", maxPoolSize=" + maxPoolSize + ", queueCapacity=" + queueCapacity
				+ ", tracingEnabled=" + tracingEnabled + ", traceFile=" + traceFile + ", keepAliveSeconds="
				+ keepAliveSeconds + ", asyncEventQueries=" + asyncEventQueries + ", threadPriority=" + threadPriority
				+ "]";
	}

}
