package fr.echoes.labs.pluginfwk.pluginloader;

import org.apache.commons.lang3.Validate;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

public class RegisteredExtensionImpl implements RegisteredExtension {

	private final String		pluginID;
	private final IExtension	extension;

	public RegisteredExtensionImpl(final String pluginID, final IExtension extension) {
		super();
		this.pluginID = pluginID;
		this.extension = extension;
		Validate.notEmpty(pluginID);
		Validate.notNull(extension);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final RegisteredExtensionImpl other = (RegisteredExtensionImpl) obj;
		if (this.extension == null) {
			if (other.extension != null) {
				return false;
			}
		} else if (!this.extension.equals(other.extension)) {
			return false;
		}
		if (this.pluginID == null) {
			if (other.pluginID != null) {
				return false;
			}
		} else if (!this.pluginID.equals(other.pluginID)) {
			return false;
		}
		return true;
	}

	@Override
	public IExtension getExtension() {
		return this.extension;
	}

	@Override
	public String getPluginID() {
		return this.pluginID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.extension == null ? 0 : this.extension.hashCode());
		result = prime * result + (this.pluginID == null ? 0 : this.pluginID.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "RegisteredExtensionImpl [pluginID=" + this.pluginID + ", extension=" + this.extension + "]";
	}

}
