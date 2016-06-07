package fr.echoes.labs.ksf.cc.plugins.dashboard.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class DashboardUrlBuilder {

	private static final String PARAM_ENTITIES = "list_entities";
	private static final String PARAM_METRICS = "list_metrics";
	private static final String PARAM_PERIOD = "custom_period";
	private static final String PARAM_OPEN = "boolean_open";
	private static final String PARAM_IN_MENU = "boolean_in_menu_bar";

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
			
			Map<String, String> urlParams = buildParams();
			
			url.append("#");
			
			for (Entry<String, String> param : urlParams.entrySet()) {				
				url
					.append(param.getKey())
					.append("=")
					.append(param.getValue())
					.append(";");				
			}
			
		}
		
		return url.toString();
	}
	
	private Map<String, String> buildParams() {
		
		Map<String, String> urlParams = Maps.newHashMap();
		urlParams.put(PARAM_ENTITIES, projectType+"_"+projectKey);
		urlParams.put(PARAM_METRICS, "");
		urlParams.put(PARAM_PERIOD, customPeriod);
		urlParams.put(PARAM_OPEN, "true");
		urlParams.put(PARAM_IN_MENU, "true");
		
		return urlParams;
	}
	
}
