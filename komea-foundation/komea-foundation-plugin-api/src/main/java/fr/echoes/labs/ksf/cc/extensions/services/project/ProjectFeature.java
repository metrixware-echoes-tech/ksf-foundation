package fr.echoes.labs.ksf.cc.extensions.services.project;

/**
 * @author dcollard
 *
 */
public class ProjectFeature implements IProjectFeature {

	private String id;
	private String subject;
	private String assignee;
	private FeatureStatus status;

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.IProjectFeature#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.IProjectFeature#getSubject()
	 */
	@Override
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.IProjectFeature#getAssignee()
	 */
	@Override
	public String getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee
	 *            the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	@Override
	public FeatureStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(FeatureStatus status) {
		this.status = status;
	}

}
