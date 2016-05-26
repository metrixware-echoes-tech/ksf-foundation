package fr.echoes.labs.ksf.cc.extensions.services.project.versions;

/**
 * @author dcollard
 *
 */
public class ProjectVersion implements IProjectVersion {

	private String id;

	private String name;

	/**
	 * @param id
	 *            the id to set
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

}
