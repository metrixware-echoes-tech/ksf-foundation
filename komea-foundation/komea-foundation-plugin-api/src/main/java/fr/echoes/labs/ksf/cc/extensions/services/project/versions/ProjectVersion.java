package fr.echoes.labs.ksf.cc.extensions.services.project.versions;

import fr.echoes.labs.ksf.cc.extensions.services.project.TicketStatus;

/**
 * @author dcollard
 *
 */
public class ProjectVersion implements IProjectVersion {

	private String id;

	private String name;

	private TicketStatus status = TicketStatus.NEW;

	/**
	 * @param id the id to set
	 *
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion#getStatus()
	 */
	@Override
	public TicketStatus getStatus() {
		return this.status;
	}

}
