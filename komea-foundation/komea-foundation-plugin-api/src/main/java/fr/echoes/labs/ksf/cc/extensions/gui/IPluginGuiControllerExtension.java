package fr.echoes.labs.ksf.cc.extensions.gui;

import fr.echoes.labs.pluginfwk.api.extension.IExtension;

/**
 * Offers the possibility to a plugin.
 *
 * @author sleroy
 *
 */
public interface IPluginGuiControllerExtension extends IExtension {

	/**
	 * Returns the domain handled by this extension ex "foreman" /springplugins/foreman/*
	 */
	String getUrlFragment();

	//
	// PluginView renderDelete(HttpServletRequest _request, HttpServletResponse _response);
	//
	// PluginView renderGet(HttpServletRequest _request, HttpServletResponse _response);
	//
	// PluginView renderPost(HttpServletRequest _request, HttpServletResponse _response);
}
