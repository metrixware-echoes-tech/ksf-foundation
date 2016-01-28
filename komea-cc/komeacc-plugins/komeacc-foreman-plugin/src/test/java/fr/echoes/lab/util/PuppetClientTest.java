package fr.echoes.lab.util;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.echoes.lab.puppet.PuppetClient;
import fr.echoes.lab.puppet.PuppetException;


@RunWith(PowerMockRunner.class)
@PrepareForTest({PuppetClient.class})
public class PuppetClientTest {

    private ProcessLaunchResult result;


	@Before
    public void setUp() throws Exception {
    	this.result = new ProcessLaunchResult();
    	
    	ExternalProcessLauncher dr = mock(ExternalProcessLauncher.class);
        PowerMockito.whenNew(ExternalProcessLauncher.class).withAnyArguments().thenReturn(dr);
        PowerMockito.when(dr.launchSync(true)).thenReturn(this.result);
    }
	
    
    @Test
    public void successfulInstallation() throws Exception {
    	this.result.setExitValue(0);
		installModule();
    }

    @Test(expected=PuppetException.class)
    public void failedInstallation() throws Exception {
		this.result.setExitValue(1);
		installModule();
    }

	private void installModule() throws PuppetException {
		final PuppetClient puppetClient = new PuppetClient();		
		puppetClient.installModule("moduleName", "moduleVersion", "environment", "modulePath");
	}

}
