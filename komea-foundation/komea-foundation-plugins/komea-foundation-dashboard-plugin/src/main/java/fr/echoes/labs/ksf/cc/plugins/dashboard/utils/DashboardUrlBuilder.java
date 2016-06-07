package fr.echoes.labs.ksf.cc.plugins.dashboard.utils;

import org.apache.commons.lang3.StringUtils;

public class DashboardUrlBuilder {

	private static final String PARAM_ENTITIES = "list_entities";
	private static final String PARAM_PERIOD = "custom_period";
	
	private String baseUrl;
	private String displayPage;
	private String projectKey;
	private String projectType;
	private String customPeriod;
	
	public DashboardUrlBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public DashboardUrlBuilder setDisplayPage(String displayPage) {
		this.displayPage = displayPage;
		return this;
	}
	
	public DashboardUrlBuilder setProjectKey(String projectKey) {
		this.projectKey = projectKey;
		return this;
	}

	public DashboardUrlBuilder setProjectType(String projectType) {
		this.projectType = projectType;
		return this;
	}
	
	public DashboardUrlBuilder setCustomPeriod(String customPeriod) {
		this.customPeriod = customPeriod;
		return this;
	}
	
	public String build() {
		
		if (StringUtils.isEmpty(this.baseUrl)) {
			return "";
		}
		
		StringBuilder url = new StringBuilder(this.baseUrl);
		
		if (!this.baseUrl.endsWith("/")) {
			url.append('/');
		}
			
		if (!StringUtils.isEmpty(this.displayPage)) {
			if (this.displayPage.startsWith("/")) {
				url.append(this.displayPage.substring(1, this.displayPage.length()));
			}else{
				url.append(this.displayPage);
			}
		}
		
		if (!StringUtils.isEmpty(this.projectKey)) {
			url.append("#").append(PARAM_ENTITIES).append("=");
			url.append(projectType).append('_').append(projectKey);
		}
			
		if (!StringUtils.isEmpty(this.customPeriod)) {
			url.append(";").append(PARAM_PERIOD).append("=").append(this.customPeriod);
		}
		
		return url.toString();
	}
	
}
