package fr.echoes.lab.ksf.cc.plugins.redmine;

/**
 * @author dcollard
 *
 */
public class RedmineIssue {

	private final Integer id;

    private String name;
	private String tracker;
    private String targetVersion;
    private String category;
    private String status;
    private String priority;
    private String assignee;


    public RedmineIssue(Integer id) {
    	this.id = id;
    }

    public Integer getId() {
    	return this.id;
    }

    public String getName() {
    	return this.name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getTracker() {
		return this.tracker;
	}

	public void setTracker(String tracker) {
		this.tracker = tracker;
	}

	public String getTargetVersion() {
		return this.targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
}
