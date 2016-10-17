package fr.echoes.labs.ksf.cc.plugins.redmine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcollard
 *
 */
public class RedmineQuery {

	private final String projectKey;
	private final int resultItemsLimit;
	private final List<Integer> trackerIds;
	private final List<Integer> statusIds;
	private final String targetVersion;

	public static class Builder {
		private String projectKey;
		private int resultItemsLimit = -1;
		private final List<Integer> trackerIds = new ArrayList<Integer>();
		private final List<Integer> statusIds = new ArrayList<Integer>();
		private String targetVersion = null;

		public Builder projectKey(String projectKey) {
			this.projectKey = projectKey;
			return this;
		}

		public Builder resultItemsLimit(int resultItemsLimit) {
			this.resultItemsLimit = resultItemsLimit;
			return this;
		}

		public Builder addTrackerId(Integer trackerId) {
			if (trackerId != null) {
				this.trackerIds.add(trackerId);
			}
			return this;
		}

		public Builder addStatusId(Integer statusId) {
			if (statusId != null) {
				this.statusIds.add(statusId);
			}
			return this;
		}

		public Builder setTargetVersion(String targetVersion) {
			this.targetVersion = targetVersion;
			return this;
		}

		public RedmineQuery build() {
			return new RedmineQuery(this);
		}
	}

	private RedmineQuery(Builder builder) {
		this.projectKey= builder.projectKey;
		this.resultItemsLimit = builder.resultItemsLimit;
		this.trackerIds = builder.trackerIds;
		this.statusIds = builder.statusIds;
		this.targetVersion = builder.targetVersion;
	}

	/**
	 * @return the project key
	 */
	public String getProjectKey() {
		return this.projectKey;
	}

	/**
	 * @return the resultItemsLimit
	 */
	public int getResultItemsLimit() {
		return this.resultItemsLimit;
	}

	/**
	 * @return the trackerId
	 */
	public List<Integer> getTrackerIds() {
		return this.trackerIds;
	}

	/**
	 * @return the statusId
	 */
	public List<Integer> getStatusIds() {
		return this.statusIds;
	}

	/**
	 * @return the target version
	 */
	public String getTargetVersion() {
		return this.targetVersion;
	}
}
