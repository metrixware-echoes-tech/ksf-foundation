package fr.echoes.labs.ksf.foreman.api.client;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.utils.ScParamsUtils;

public class DeleteMe {

	@Test
	@Ignore
	public void deleteMe() throws IOException {
		
		final ForemanClient foreman = new ForemanClient("https://10.1.10.17/foreman", "admin", "JV3nU3aTJ2AtLhsG");
		
		final String rawValue = "<%= @host.shortname  %>:\r\n  keep: 1D\r\n  type: remote\r\n  user: backupninja\r\n  home: \"/var/backups/<%= @host.domain  %>/<%= @host.shortname  %>\"\r\n  host: haproxy.<%= @host.domain  %>";
		
		System.out.println(ScParamsUtils.toHash(rawValue));
		
		final SmartClassParameter scParam = foreman.getSmartClassParameter(721);
		
		System.out.println("type: "+scParam.getType());
		System.out.println("Value: "+scParam.getOverrideValues());
		
//		foreman.updateSmartClassParameter(param);
	}
	
}
