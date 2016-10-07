package fr.echoes.labs.foremanapi.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

	private Integer id;
	
	private String uuid;
	
	private String name;
	
	@JsonProperty("operatingsystem_id")
	private Integer operatingSystemId;
	
	@JsonProperty("operatingsystem_name")
	private String operatingSystemName;
	
	@JsonProperty("compute_resource_id")
	private Integer computeResourceId;
	
	@JsonProperty("architecture_id")
	private Integer architectureId;
	
	@JsonProperty("architecture_name")
	private String architectureName;
	
	private String username;

	public Integer getId() {
		return id;
	}	

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public Integer getOperatingSystemId() {
		return operatingSystemId;
	}

	public void setOperatingSystemId(Integer operatingSystemId) {
		this.operatingSystemId = operatingSystemId;
	}

	public String getOperatingSystemName() {
		return operatingSystemName;
	}
	
	public void setOperatingSystemName(String operatingSystemName) {
		this.operatingSystemName = operatingSystemName;
	}

	public Integer getComputeResourceId() {
		return computeResourceId;
	}

	public void setComputeResourceId(Integer computeResourceId) {
		this.computeResourceId = computeResourceId;
	}
	
	public Integer getArchitectureId() {
		return architectureId;
	}
	
	public void setArchitectureId(Integer architectureId) {
		this.architectureId = architectureId;
	}
	
	public String getArchitectureName() {
		return architectureName;
	}

	public void setArchitectureName(String architectureName) {
		this.architectureName = architectureName;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}	
	
}
