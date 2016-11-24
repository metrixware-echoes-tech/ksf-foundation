package fr.echoes.labs.ksf.foreman;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;
import java.util.Map.Entry;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.echoes.labs.ksf.foreman.actions.BackupAction;
import fr.echoes.labs.ksf.foreman.actions.IAction;
import fr.echoes.labs.ksf.foreman.actions.InstallAction;
import fr.echoes.labs.ksf.foreman.api.client.ForemanClient;
import fr.echoes.labs.ksf.foreman.api.client.ForemanClientProxy;
import fr.echoes.labs.ksf.foreman.backup.BackupStorage;
import fr.echoes.labs.ksf.foreman.backup.PuppetModulesBackupService;
import fr.echoes.labs.ksf.foreman.backup.SmartClassParameterBackupService;
import fr.echoes.labs.ksf.foreman.backup.SmartVariableBackupService;

public class Launcher {

	public static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
	
	private static final String ACTION_BACKUP = "backup";
	private static final String ACTION_INSTALL = "install";
	
	private final CommandLineOptions options;
	
	private ForemanClient foreman;
	private BackupStorage backupStorage;
	private SmartClassParameterBackupService smartParamBackupdService;
	private PuppetModulesBackupService hostPuppetModulesBackupService;
	private SmartVariableBackupService smartVariableBackupService;
	
	public Launcher(final CommandLineOptions options) {
		this.options = options;
	}
	
	public static void main(final String[] args) throws IOException {

		final CommandLineOptions options = new CommandLineOptions();
		final CmdLineParser parser = new CmdLineParser(options);

		try {
			parser.parseArgument(args);

		} catch (final CmdLineException ex) {
			LOGGER.error("invalid arguments: {}", ex.getMessage());
			parser.printUsage(System.out);
			throw new InvalidParameterException();
		}
		
		new Launcher(options).init().run();	
	}
	
	public Launcher init() throws MalformedURLException {
		
		this.foreman = new ForemanClientProxy(options.getForemanUrl(), options.getForemanUser(), options.getForemanPassword());
		
		this.backupStorage = new BackupStorage(options.getBackupFolder()); 
		this.smartParamBackupdService = new SmartClassParameterBackupService(backupStorage);
		this.hostPuppetModulesBackupService = new PuppetModulesBackupService(backupStorage);
		this.smartVariableBackupService = new SmartVariableBackupService(backupStorage);
		
		return this;
	}
	
	public void run() throws IOException {
		
		// execute action
		resolveAction(options.getMode()).execute();
		
		// display stats
		for (Entry<String, Integer> entry : foreman.getNbRequests().entrySet()) {
			LOGGER.info("Nb requests {} executed: {}", entry.getKey(), entry.getValue());
		}
		
	}
	
	public IAction resolveAction(final String mode) {
		
		if (ACTION_BACKUP.equalsIgnoreCase(mode)) {
			return new BackupAction(foreman, smartParamBackupdService, hostPuppetModulesBackupService, smartVariableBackupService);
		}else if (ACTION_INSTALL.equalsIgnoreCase(mode)) {
			return new InstallAction(foreman, smartParamBackupdService, hostPuppetModulesBackupService,smartVariableBackupService, backupStorage);
		}else{
			throw new IllegalArgumentException(mode+" is not a valid mode.");
		}
		
	}
	
}
