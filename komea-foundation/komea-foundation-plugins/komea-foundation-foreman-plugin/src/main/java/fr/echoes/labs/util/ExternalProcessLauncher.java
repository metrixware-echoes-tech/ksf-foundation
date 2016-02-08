package fr.echoes.labs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExternalProcessLauncher implements IExternalProcessLauncher {

	final static Logger LOGGER = LoggerFactory
			.getLogger(ExternalProcessLauncher.class);

	private String workingDirectory = null;
	private final ProcessBuilder processBuilder;
	private final List<String> commands;

	public ExternalProcessLauncher(List<String> commands) {
		if (commands == null) {
			throw new IllegalArgumentException();
		}
		logDebug("Creating processBuilder"); //$NON-NLS-1$
		this.commands = commands;
		this.processBuilder = new ProcessBuilder(commands);
	}

	@Override
	public void setWorkingDirectory(String directory) {
		this.workingDirectory = directory;
	}

	@Override
	public IProcessLaunchResult launchSync(boolean redirectErrorStream)
			throws ExternalProcessLauncherException {
		logDebug("Command line: " + this.commands); //$NON-NLS-1$
		final ProcessLaunchResult launchResult = new ProcessLaunchResult();

		this.processBuilder.redirectErrorStream(redirectErrorStream);

		if (this.workingDirectory != null) {
			this.processBuilder.directory(new File(this.workingDirectory));
		}
		InputStream errorStream = null;
		InputStream inputStream = null;
		StreamGobbler errorStreamGobbler = null;
		StreamGobbler inputStreamGobbler = null;

		try {
			logDebug("Starting processBuilder"); //$NON-NLS-1$
			final Process process = this.processBuilder.start();

			errorStream = process.getErrorStream();
			inputStream = process.getInputStream();

			errorStreamGobbler = new StreamGobbler(
					"ErrorStreamGobbler", errorStream); //$NON-NLS-1$
			inputStreamGobbler = new StreamGobbler(
					"InputStreamGobbler", inputStream); //$NON-NLS-1$

			logDebug("Starting errorStreamGobbler: " + errorStreamGobbler.getDebugDisplayName()); //$NON-NLS-1$
			errorStreamGobbler.start();
			logDebug("Starting inputStreamGobbler: " + inputStreamGobbler.getDebugDisplayName()); //$NON-NLS-1$
			inputStreamGobbler.start();

			logDebug("Waiting for process: [" + process + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			final int waitFor = process.waitFor();
			logDebug("Process: [" + process + "] exit value=" + waitFor); //$NON-NLS-1$ //$NON-NLS-2$
			launchResult.setExitValue(waitFor);
		} catch (final IOException e) {
			throw new ExternalProcessLauncherException(e);
		} catch (final InterruptedException e) {
			throw new ExternalProcessLauncherException(e);
		} finally {
			try {
				if (errorStreamGobbler != null) {
					errorStreamGobbler.join();
				}
				if (inputStreamGobbler != null) {
					inputStreamGobbler.join();
				}
			} catch (final Exception e) {
				// Do nothing
			}
			try {
				if (errorStream != null) {
					errorStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (final IOException e) {
				// Do nothing
			}
		}
		final List<String> streamInputLines = inputStreamGobbler
				.getStreamOutputLines();
		final List<String> streamErrorLines = errorStreamGobbler
				.getStreamOutputLines();
		launchResult.setInputStream(streamInputLines);
		launchResult.setErrorStream(streamErrorLines);
		return launchResult;
	}

	private void logDebug(final String message) {
		LOGGER.debug(message);
	}

	@Override
	public IProcessLaunchResult launchSync()
			throws ExternalProcessLauncherException {
		return launchSync(false);
	}

	@Override
	public Map<String, String> getEnvironements() {
		return this.processBuilder.environment();
	}
}
