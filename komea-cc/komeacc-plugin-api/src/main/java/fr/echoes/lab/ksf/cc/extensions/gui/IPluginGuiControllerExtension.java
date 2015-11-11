package fr.echoes.lab.ksf.cc.extensions.gui;

/**
 * Offers the possibility to a plugin.
 *
 * @author sleroy
 *
 */
public interface IPluginGuiControllerExtension {
	/**
	 * Returns the domain handled by this extension ex "foreman" /plugins/foreman/*
	 */
	String getUrlFragment();


	//
	//	PluginView renderDelete(HttpServletRequest _request, HttpServletResponse _response);
	//
	//	PluginView renderGet(HttpServletRequest _request, HttpServletResponse _response);
	//
	//	PluginView renderPost(HttpServletRequest _request, HttpServletResponse _response);
}
