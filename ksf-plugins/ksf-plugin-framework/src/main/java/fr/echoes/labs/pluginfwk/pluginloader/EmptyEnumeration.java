package fr.echoes.labs.pluginfwk.pluginloader;

import java.net.URL;
import java.util.Enumeration;

public class EmptyEnumeration implements Enumeration<URL> {

	@Override
	public boolean hasMoreElements() {
		return false;
	}

	@Override
	public URL nextElement() {
		return null;
	}

}
