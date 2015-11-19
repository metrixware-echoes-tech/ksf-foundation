package fr.echoes.lab.puppet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.lab.util.ExternalProcessLauncher;
import fr.echoes.lab.util.ExternalProcessLauncherException;
import fr.echoes.lab.util.IProcessLaunchResult;



public class PuppetClient {

	final static Logger LOGGER = LoggerFactory
			.getLogger(PuppetClient.class);

	private static final String PUPPET_BIN = "puppet";
	private static final String MODULE = "module";
	private static final String INSTALL = "install";
	private static final String OPT_ENVIRONMENT = "--environment";
	private static final String OPT_VERSION = "--version";
	private static final String OPT_MODULE_PATH = "--modulepath";
	private static final String SUDO = "sudo";


	public PuppetClient() {

	}

	/**
	 * Installs the given puppet module. Throws a {@link PuppetException} if the installation failed.
	 *
	 * @param name the module name. cannot be {@code null}.
	 * @param version the module version. can be {@code null}.
	 * @param environment the destination environment . can be {@code null}.
	 * @throws PuppetException
	 */
	public void installModule(String name, String version, String environment, String modulePath) throws PuppetException {

		if (name == null) {
			throw new NullPointerException("Puppet module name cannot be null");
		}

		LOGGER.info("Installing Puppet module. Name: {} Version: {} Environment: {}", name, version, environment);

		String targetPath = null;

		if (modulePath != null && environment != null) {
			final File file = new File(modulePath, environment);
			LOGGER.error("[puppet] Installing module into {}", file.getAbsolutePath());
			final List<String> commandMkdir = new ArrayList<>();
			commandMkdir.add(SUDO);
			commandMkdir.add("mkdir");
			commandMkdir.add("-p");
			commandMkdir.add( file.getAbsolutePath());
			final ExternalProcessLauncher processLauncher = new ExternalProcessLauncher(commandMkdir);
			try {
				final IProcessLaunchResult process = processLauncher.launchSync(true);
				if (process.getExitValue() != 0) {
					LOGGER.error("[puppet] Failed to create directory {}", file.getAbsoluteFile());
				}
			} catch (final ExternalProcessLauncherException e) {
				LOGGER.error("[puppet] Failed to create directory {}", file.getAbsoluteFile());
			}
			targetPath = new File(modulePath, environment).toString();
			targetPath = new File(targetPath, "modules").toString();
		}



		final List<String> commandLine = new ArrayList<>();
		commandLine.add(SUDO);
		commandLine.add(PUPPET_BIN);
		commandLine.add(MODULE);
		commandLine.add(INSTALL);
		commandLine.add(name);
		if (version != null) {
			commandLine.add(OPT_VERSION);
			commandLine.add(version);
		}
		if (environment != null) {
			commandLine.add(OPT_ENVIRONMENT);
			commandLine.add(environment);
		}

		if (targetPath != null) {
			commandLine.add(OPT_MODULE_PATH);
			commandLine.add(targetPath);
		}

		final ExternalProcessLauncher processLauncher = new ExternalProcessLauncher(commandLine);
		try {
			final IProcessLaunchResult process = processLauncher.launchSync(true);
			if (process.getExitValue() != 0) {
				final PuppetException exception = new PuppetException("Failed to install Puppet module");
				final String stderr = StringUtils.join(process.getInputStreamLines(), '\n');
				LOGGER.error(stderr, exception);
				throw exception;
			}
		} catch (final ExternalProcessLauncherException e) {
			throw new PuppetException(e);
		}


	}


}
