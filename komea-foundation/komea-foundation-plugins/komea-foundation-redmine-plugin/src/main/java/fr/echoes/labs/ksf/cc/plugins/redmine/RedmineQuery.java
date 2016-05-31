package fr.echoes.labs.ksf.cc.plugins.redmine;

/**
 * @author dcollard
 *
 */
public class RedmineQuery {

	private final String projectName;
	private final int resultItemsLimit;
	private final int trackerId;
	private final int statusId;

	public static class Builder {
		private String projectName;
		private int resultItemsLimit = -1;
		private int trackerId = -1;
		private int statusId = -1;

		public Builder projectName(String projectName) {
			this.projectName = projectName;
			return this;
		}

		public Builder resultItemsLimit(int resultItemsLimit) {
			this.resultItemsLimit = resultItemsLimit;
			return this;
		}

		public Builder trackerId(int trackerId) {
			this.trackerId = trackerId;
			return this;
		}

		public Builder statusId(int statusId) {
			this.statusId = statusId;
			return this;
		}

		public RedmineQuery build() {
			return new RedmineQuery(this);
		}
	}

	private RedmineQuery(Builder builder) {
		this.projectName = builder.projectName;
		this.resultItemsLimit = builder.resultItemsLimit;
		this.trackerId = builder.trackerId;
		this.statusId = builder.statusId;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return this.projectName;
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
	public int getTrackerId() {
		return this.trackerId;
	}

	/**
	 * @return the statusId
	 */
	public int getStatusId() {
		return this.statusId;
	}
}
