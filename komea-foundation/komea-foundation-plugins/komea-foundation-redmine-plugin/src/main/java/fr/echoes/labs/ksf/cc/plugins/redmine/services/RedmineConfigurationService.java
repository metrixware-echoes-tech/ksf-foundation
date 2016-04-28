package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dcollard
 *
 */
@Service("redmineConfiguration")
public class RedmineConfigurationService {

    @Value("${ksf.redmine.url}")
    private String url;

    @Value("${ksf.redmine.apiAccessKey}")
    private String appiAccessKey;
    
    @Value("${ksf.redmine.resultItemsLimit}")
    private int resultItemsLimit;

    @Value("${ksf.redmine.featureTrackerId}")
    private int featureTrackerId;

    @Value("${ksf.redmine.featureStatusNewId}")
    private int featureStatusNewId;    
    
    @Value("${ksf.redmine.featureStatusAssignedId}")
    private int featureStatusAssignedId;
    
    @Value("${ksf.redmine.featureStatusClosedId}")
    private int featureStatusClosedId;    
      
    
	public String getUrl() {
        if ('/' == this.url.charAt(this.url.length() - 1)) {
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
		return resultItemsLimit;
	}

	/**
	 * @return the featureTrackerId
	 */
	public int getFeatureTrackerId() {
		return featureTrackerId;
	}

	/**
	 * @return the featureStatusClosedId
	 */
	public int getFeatureStatusClosedId() {
		return featureStatusClosedId;
	}

	/**
	 * @return the featureStatusNewId
	 */
	public int getFeatureStatusNewId() {
		return featureStatusNewId;
	}

	/**
	 * @return the featureStatusAssignedId
	 */
	public int getFeatureStatusAssignedId() {
		return featureStatusAssignedId;
	}
}
