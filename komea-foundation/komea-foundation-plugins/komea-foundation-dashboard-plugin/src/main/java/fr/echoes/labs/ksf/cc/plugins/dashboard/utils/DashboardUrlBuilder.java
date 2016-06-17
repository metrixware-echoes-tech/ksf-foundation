package fr.echoes.labs.ksf.cc.plugins.dashboard.utils;

import org.apache.commons.lang3.StringUtils;

public class DashboardUrlBuilder {

	private String baseUrl;
	private String projectKey;
	
	public DashboardUrlBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}
	
	public DashboardUrlBuilder setProjectKey(String projectKey) {
		this.projectKey = projectKey;
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
		
		return url.append("web/").append(projectKey).toString();
	}
	
}
