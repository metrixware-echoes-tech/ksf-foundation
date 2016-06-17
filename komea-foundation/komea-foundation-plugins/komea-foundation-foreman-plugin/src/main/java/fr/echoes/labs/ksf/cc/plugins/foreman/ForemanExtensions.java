package fr.echoes.labs.ksf.cc.plugins.foreman;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import fr.echoes.labs.ksf.extensions.api.IExtension;

public class ForemanExtensions {
	@Inject
	private Set<IExtension> extensions = new HashSet<>();

	public ForemanExtensions() {
	}

	public Set<IExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(final Set<IExtension> _extensions) {
		extensions = _extensions;
	}
}
