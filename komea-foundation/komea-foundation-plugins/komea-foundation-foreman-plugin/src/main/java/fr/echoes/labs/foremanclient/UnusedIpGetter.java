package fr.echoes.labs.foremanclient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.util.ExternalProcessLauncher;
import fr.echoes.labs.util.ExternalProcessLauncherException;
import fr.echoes.labs.util.IProcessLaunchResult;

public class UnusedIpGetter {

	final static Logger LOGGER = LoggerFactory.getLogger(UnusedIpGetter.class);

	private final String scriptPath;

    private static final String IPADDRESS_PATTERN =
		".*\"(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])).*";

	public UnusedIpGetter(String scriptPath) {
		LOGGER.debug("[foreman] unused IP script : '{}'", scriptPath);
		this.scriptPath = scriptPath;
	}



	public String get() throws Exception {

		final List<String> commandLine = new ArrayList<>(1);

		commandLine.add(this.scriptPath);

		LOGGER.info("[foreman] unused IP script commandLine:" + StringUtils.join(commandLine, ' '));

		final ExternalProcessLauncher processLauncher = new ExternalProcessLauncher(commandLine);
		try {
			final IProcessLaunchResult process = processLauncher.launchSync(true);
			final String scriptOutput = StringUtils.join(process.getInputStreamLines(), '\n');
			if (process.getExitValue() != 0) {
				LOGGER.error(scriptOutput);
				return StringUtils.EMPTY;
			}
			return extractIp(scriptOutput);
		} catch (final ExternalProcessLauncherException e) {
			LOGGER.error("Failed to get a unused IP", e);
			return StringUtils.EMPTY;
		}

	}

	private String extractIp(String scriptOutput) {
		final Pattern ipPattern = Pattern.compile(IPADDRESS_PATTERN) ;
		final Matcher m = ipPattern.matcher(scriptOutput) ;
		final String ip;
		if (m.matches()) {
			ip = m.group(1);
		} else {
			ip = "";
			LOGGER.error("IP address not found in " + scriptOutput);
		}
		return ip;
	}


}
