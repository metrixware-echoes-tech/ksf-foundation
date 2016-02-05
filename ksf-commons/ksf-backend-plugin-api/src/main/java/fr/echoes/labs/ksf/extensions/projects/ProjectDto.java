package fr.echoes.lab.ksf.extensions.projects;

import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;

public class ProjectDto {
	private String id;

	private String	key;
	private String	name;

	private String statusId;

	private String categoryId;

	private String ownerId;

	private String description;

	private URL image;

	private List<String> tags = Lists.newArrayList();

	public String getCategoryId() {
		return categoryId;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public URL getImage() {
		return image;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getStatusId() {
		return statusId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setCategoryId(final String categoryId) {
		this.categoryId = categoryId;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setImage(final URL image) {
		this.image = image;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setOwnerId(final String ownerId) {
		this.ownerId = ownerId;
	}

	public void setStatusId(final String statusId) {
		this.statusId = statusId;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Project{" + "id=" + id + ", key=" + key + ", name=" + name + ", statusId=" + statusId + ", categoryId="
				+ categoryId + ", ownerId=" + ownerId + ", description=" + description + ", image=" + image + ", tags="
				+ tags + '}';
	}

}
