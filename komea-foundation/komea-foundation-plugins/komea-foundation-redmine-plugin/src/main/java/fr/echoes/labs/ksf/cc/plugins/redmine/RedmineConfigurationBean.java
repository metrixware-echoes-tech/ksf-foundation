package fr.echoes.labs.ksf.cc.plugins.redmine;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public class RedmineConfigurationBean {

	@Value("${ksf.redmine.url}")
    private String url;

    @Value("${ksf.redmine.apiAccessKey}")
    private String appiAccessKey;

    @Value("${ksf.redmine.resultItemsLimit}")
    private int resultItemsLimit;

    @Value("${ksf.redmine.bugTrackerId:1}")
    private int bugTrackerId;

    @Value("${ksf.redmine.featureTrackerId:2}")
    private int featureTrackerId;

    @Value("${ksf.redmine.featureStatusNewId}")
    private int featureStatusNewId;

    @Value("${ksf.redmine.featureStatusAssignedId}")
    private int featureStatusAssignedId;

    @Value("${ksf.redmine.featureStatusResolvedId}")
    private int featureStatusResolvedId;

    @Value("${ksf.redmine.featureStatusClosedId}")
    private int featureStatusClosedId;

    @Value("${ksf.redmine.featureResolutionRejectedValue}")
    private String featureResolutionRejectedValue;

    @Value("${ksf.redmine.customField.resolution.id}")
    private int resolutionFieldId;

    @Value("${ksf.redmine.releaseTicketMessagePattern}")
    private String releaseTicketMessagePattern;

    @Value("${ksf.redmine.adminUsername}")
    private String adminUserName;

//    @Value("#{'${ksf.redmine.featureIds}'.split(',')}")
    @Value("${ksf.redmine.featureIds}")
    private List<Integer> featureIds;

    @Value("${ksf.redmine.hackBugApi}")
    private boolean hackBugApi;

    @Value("${ksf.redmine.taskTrackerId}")
    private Integer taskTrackerId;

    public String getUrl() {
        if (url != null && '/' == this.url.charAt(this.url.length() - 1)) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
        return this.url;
    }

    /**
     * @return the appiAccessKey
     */
    public String getApiAccessKey() {
        return this.appiAccessKey;
    }

    /**
     * @return the resultItemsLimit
     */
    public int getResultItemsLimit() {
        return this.resultItemsLimit;
    }

    /**
     * @return the featureTrackerId
     */
    public int getFeatureTrackerId() {
        return this.featureTrackerId;
    }

    /**
     * @return the featureStatusResolvedId
     */
    public int getFeatureStatusResolvedId() {
        return this.featureStatusResolvedId;
    }

    /**
     * @return the featureStatusClosedId
     */
    public int getFeatureStatusClosedId() {
        return this.featureStatusClosedId;
    }

    /**
     * @return the featureResolutionRejectedValue
     */
    public String getFeatureResolutionRejectedValue() {
        return this.featureResolutionRejectedValue;
    }

    /**
     * @return the resolutionFieldId
     */
    public int getResolutionFieldId() {
        return this.resolutionFieldId;
    }

    /**
     * @return the featureStatusNewId
     */
    public int getFeatureStatusNewId() {
        return this.featureStatusNewId;
    }

    /**
     * @return the featureStatusAssignedId
     */
    public int getFeatureStatusAssignedId() {
        return this.featureStatusAssignedId;
    }

    /**
     * @return the releaseTicketMessagePattern
     */
    public String getReleaseTicketMessagePattern() {
        return this.releaseTicketMessagePattern;
    }

    /**
     * @return the adminUserName
     */
    public String getAdminUserName() {
        return this.adminUserName;
    }

    /**
     * @return the bugTrackerId
     */
    public int getBugTrackerId() {
        return this.bugTrackerId;
    }

    /**
     * @return the featureIds
     */
    public List<Integer> getFeatureIds() {
        return this.featureIds;
    }

    /**
     * @return {@code true} to avoid the server error when calling the Redmine
     * API to get a issue with its ID.
     */
    public boolean isHackBugApi() {
        return this.hackBugApi;
    }

    /**
     * @return the taskTrackerId
     */
    public Integer getTaskTrackerId() {
        return this.taskTrackerId;
    }

	public String getAppiAccessKey() {
		return appiAccessKey;
	}
	

	public void setAppiAccessKey(String appiAccessKey) {
		this.appiAccessKey = appiAccessKey;
	}
	

	public void setUrl(String url) {
		this.url = url;
	}
	

	public void setResultItemsLimit(int resultItemsLimit) {
		this.resultItemsLimit = resultItemsLimit;
	}
	

	public void setBugTrackerId(int bugTrackerId) {
		this.bugTrackerId = bugTrackerId;
	}
	

	public void setFeatureTrackerId(int featureTrackerId) {
		this.featureTrackerId = featureTrackerId;
	}
	

	public void setFeatureStatusNewId(int featureStatusNewId) {
		this.featureStatusNewId = featureStatusNewId;
	}
	

	public void setFeatureStatusAssignedId(int featureStatusAssignedId) {
		this.featureStatusAssignedId = featureStatusAssignedId;
	}
	

	public void setFeatureStatusResolvedId(int featureStatusResolvedId) {
		this.featureStatusResolvedId = featureStatusResolvedId;
	}
	

	public void setFeatureStatusClosedId(int featureStatusClosedId) {
		this.featureStatusClosedId = featureStatusClosedId;
	}
	

	public void setFeatureResolutionRejectedValue(String featureResolutionRejectedValue) {
		this.featureResolutionRejectedValue = featureResolutionRejectedValue;
	}
	

	public void setResolutionFieldId(int resolutionFieldId) {
		this.resolutionFieldId = resolutionFieldId;
	}
	

	public void setReleaseTicketMessagePattern(String releaseTicketMessagePattern) {
		this.releaseTicketMessagePattern = releaseTicketMessagePattern;
	}
	

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	

	public void setFeatureIds(List<Integer> featureIds) {
		this.featureIds = featureIds;
	}
	

	public void setHackBugApi(boolean hackBugApi) {
		this.hackBugApi = hackBugApi;
	}
	

	public void setTaskTrackerId(Integer taskTrackerId) {
		this.taskTrackerId = taskTrackerId;
	}
	
}
