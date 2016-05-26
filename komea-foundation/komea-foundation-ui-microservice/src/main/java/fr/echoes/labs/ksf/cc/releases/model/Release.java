package fr.echoes.labs.ksf.cc.releases.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tocea.corolla.products.domain.Project;

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

	@DBRef
	private Project project;

	@NotBlank
	private String releaseId;

	public String getId() {
		return this.id;
	}

	public void setId(final String _id) {
		this.id = _id;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
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

}
