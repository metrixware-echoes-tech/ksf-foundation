package fr.echoes.lab.ksf.cc.plugins.redmine;

/**
 * @author dcollard
 *
 */
public class RedmineQuery {

	private final String projectName;

	public static class Builder {
		private String projectName;

		public Builder projectName(String projectName) {
			this.projectName = projectName;
			return this;
		}

		public RedmineQuery build() {
			return new RedmineQuery(this);
		}
	}

	private RedmineQuery(Builder builder) {
		this.projectName = builder.projectName;
	}

	public String getProjectName() {
		return this.projectName;
	}

}
