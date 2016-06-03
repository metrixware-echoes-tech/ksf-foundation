package fr.echoes.labs.ksf.cc.extensions.services.project.versions;

import fr.echoes.labs.ksf.cc.extensions.services.project.TicketStatus;


/**
 * @author dcollard
 *
 */
public interface IProjectVersion {

	/**
	 * @return the id
	 */
	public String getId();

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @return the status
	 */
	public TicketStatus getStatus();


}
