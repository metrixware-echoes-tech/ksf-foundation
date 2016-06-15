package fr.echoes.labs.ksf.cc.plugins.nexus.extensions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType (XmlAccessType.FIELD)
public class RepositoryData {

	private String id;
	private String name;
	private Boolean exposed;
	private String repoType;
	private String repoPolicy;
	private String providerRole;
	private String provider;
	private String format;


	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getExposed() {
		return this.exposed;
	}
	public void setExposed(Boolean exposed) {
		this.exposed = exposed;
	}
	public String getRepoType() {
		return this.repoType;
	}
	public void setRepoType(String repoType) {
		this.repoType = repoType;
	}
	public String getRepoPolicy() {
		return this.repoPolicy;
	}
	public void setRepoPolicy(String repoPolicy) {
		this.repoPolicy = repoPolicy;
	}
	public String getProviderRole() {
		return this.providerRole;
	}
	public void setProviderRole(String providerRole) {
		this.providerRole = providerRole;
	}
	public String getProvider() {
		return this.provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getFormat() {
		return this.format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
}
