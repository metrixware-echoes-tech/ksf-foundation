package fr.echoes.labs.ksf.cc.extensions.services.project.features;

import fr.echoes.labs.ksf.cc.extensions.services.project.TicketStatus;

/**
 * @author dcollard
 *
 */
public interface IProjectFeature {

	/**
	 * @return the id
	 */
	public String getId();

	/**
	 * @return the subject
	 */
	public String getSubject();

	/**
	 * @return the assignee
	 */
	public String getAssignee();

	/**
	 * @return the status
	 */
	public TicketStatus getStatus();

}