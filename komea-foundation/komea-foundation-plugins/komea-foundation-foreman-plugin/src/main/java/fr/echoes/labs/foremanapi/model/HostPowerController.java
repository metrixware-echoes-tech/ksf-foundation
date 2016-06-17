package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class HostPowerController {

	public static class PowerStatus {
        @JsonProperty public String power;
    }

	public static class PowerAction {
		@JsonProperty public String power_action;
	}

}
