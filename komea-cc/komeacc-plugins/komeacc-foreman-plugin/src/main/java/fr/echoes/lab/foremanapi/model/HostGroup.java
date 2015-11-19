package fr.echoes.lab.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HostGroup {

	@JsonProperty("name") 				public String name;
	@JsonProperty("id") 				public String id;
	@JsonProperty("operatingsystem_id") public Integer operatingSystemId;
	@JsonProperty("domain_id") 			public Integer domainId;
	@JsonProperty("environment_id") 	public Integer environmentId;
	@JsonProperty("ancestry") 			public String ancestry;
	@JsonProperty("title") 				public String title;
//	@JsonProperty("parameters")       	public ArrayList<Map<String, String>> parameters;
	@JsonProperty("puppetclass_ids") 	public Integer[] puppetclassIds;
	@JsonProperty("subnet_name") 		public String subnet_name = "KSF ADS";
	@JsonProperty("ptable_id")          public String ptable_id;
	@JsonProperty("subnet_id")          public String subnet_id;
	@JsonProperty("medium_id")          public String medium_id;


}
