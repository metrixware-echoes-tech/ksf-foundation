package fr.echoes.labs.ksf.cc.extensions.services.project;

/**
 * @author dcollard
 *
 */
public interface IProjectFeature {

	/**
	 * @return the id
	 */
	public abstract String getId();

	/**
	 * @return the subject
	 */
	public abstract String getSubject();

	/**
	 * @return the assignee
	 */
	public abstract String getAssignee();

}