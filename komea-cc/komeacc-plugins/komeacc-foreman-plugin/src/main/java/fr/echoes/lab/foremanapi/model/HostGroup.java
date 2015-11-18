package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HostGroup {

	@JsonProperty("name") 				public String name;
	@JsonProperty("id") 				public String id;
	@JsonProperty("subnet_id") 			public Integer subnetId;
	@JsonProperty("operatingsystem_id") public Integer operatingSystemId;
	@JsonProperty("domain_id") 			public Integer domainId;
	@JsonProperty("environment_id") 	public Integer environmentId;
	@JsonProperty("ancestry") 			public String ancestry;
	@JsonProperty("title") 				public String title;
//	@JsonProperty("parameters")       	public ArrayList<Map<String, String>> parameters;
	@JsonProperty("puppetclass_ids") 	public Integer[] puppetclassIds;
	@JsonProperty("subnet_name") 		public String subnet_name = "KSF ADS";


}
