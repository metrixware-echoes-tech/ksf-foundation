package fr.echoes.labs.ksf.cc.plugins.redmine.model;

/**
 * @author dcollard
 *
 */
public class RedmineIssue {

	private final Integer id;

    private String subject;
	private String tracker;
    private String targetVersion;
    private String category;
    private String status;
    private String priority;
    private String assignee;

	private Integer statusId;


    public RedmineIssue(Integer id) {
    	this.id = id;
    }

    public Integer getId() {
    	return this.id;
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

	public String getSubject() {
		return this.subject;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
}
