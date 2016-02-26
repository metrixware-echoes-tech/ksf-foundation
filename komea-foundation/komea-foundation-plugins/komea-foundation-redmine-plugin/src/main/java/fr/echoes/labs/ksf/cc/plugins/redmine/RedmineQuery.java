package fr.echoes.labs.ksf.cc.plugins.redmine;

/**
 * @author dcollard
 *
 */
public class RedmineQuery {

	private final String projectName;
	private int resultItemsLimit;

	public static class Builder {
		private String projectName;
		private int resultItemsLimit;

		public Builder projectName(String projectName) {
			this.projectName = projectName;
			return this;
		}
		
		public Builder resultItemsLimit(int resultItemsLimit) {
			this.resultItemsLimit = resultItemsLimit;
			return this;
		}

		public RedmineQuery build() {
			return new RedmineQuery(this);
		}
	}

	private RedmineQuery(Builder builder) {
		this.projectName = builder.projectName;
		this.resultItemsLimit = builder.resultItemsLimit;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public int getResultItemsLimit() {
		return resultItemsLimit;
	}

}
