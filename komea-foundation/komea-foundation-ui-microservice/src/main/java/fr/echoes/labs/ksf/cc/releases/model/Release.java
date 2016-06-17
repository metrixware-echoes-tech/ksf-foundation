package fr.echoes.labs.ksf.cc.releases.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author dcollard
 *
 */
@Document
public class Release implements Serializable {

	private static final long serialVersionUID = 5378134149941741583L;

	@Id
	@Field("_id")
	private String id;

	@NotEmpty
	private String projectId;

	@NotBlank
	private String releaseId;

	@NotBlank
	private String releaseVersion;

	private ReleaseState state;

	public String getId() {
		return this.id;
	}

	public void setId(final String _id) {
		this.id = _id;
	}

	/**
	 * @return the releaseId
	 */
	public String getReleaseId() {
		return this.releaseId;
	}

	/**
	 * @param releaseId the releaseId to set
	 */
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	@Override
	public String toString() {
		return "Release [id=" + this.id + ", releaseId=" + this.getReleaseId() + "]";
	}

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return this.projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the releaseVersion
	 */
	public String getReleaseVersion() {
		return this.releaseVersion;
	}

	/**
	 * @param releaseVersion the releaseVersion to set
	 */
	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	/**
	 * @param state the state
	 */
	public void setState(ReleaseState state) {
		this.state = state;
	}


	/**
	 * @return the state
	 */
	public ReleaseState getState() {
		return this.state;
	}

}
