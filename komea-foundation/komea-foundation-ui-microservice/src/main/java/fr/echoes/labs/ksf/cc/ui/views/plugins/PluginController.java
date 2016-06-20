/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.cc.ui.views.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.echoes.labs.ksf.cc.plugins.PluginServiceLoader;
import fr.echoes.labs.pluginfwk.api.plugin.PluginManager;

/**
 * This service delegates the render to plugins.
 *
 * @author sleroy
 */
@Controller
public class PluginController {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(PluginController.class);

	@Autowired
	private PluginManager		pluginManager;

	@Autowired
	private PluginServiceLoader	pluginServiceLoader;

	/**
	 * Une direction est produite pour être capturée par Spring Security et afficher la page de login
	 *
	 * @return
	 */
	@RequestMapping(value = { "/ui/plugins" })
	public ModelAndView pluginPage() {
		final ModelAndView modelAndView = new ModelAndView("plugins");
		modelAndView.addObject("plugins", this.pluginManager.getPluginInformations());
		return modelAndView;
	}

	/**
	 * Une direction est produite pour être capturée par Spring Security et afficher la page de login
	 *
	 * @return
	 */
	@RequestMapping(value = { "/ui/plugins/reload" })
	public ModelAndView reloadPlugins() {
		this.pluginServiceLoader.reloadPlugins();
		return this.pluginPage();
	}

	//
	// @ResponseStatus(value = HttpStatus.NOT_FOUND)
	// @ExceptionHandler({ Exception.class })
	// public void handlePageNotFoundException(final Exception e) {
	// LOGGER.error("Could not render the plugin page", e);
	// }
	//
	// @RequestMapping(value = "/ui/{pluginID}/*", method = RequestMethod.DELETE)
	// public ModelAndView resolveDelete(@PathVariable final String pluginID, final HttpServletRequest _request,
	// final HttpServletResponse _response) {
	// final IPluginGuiControllerExtension guiController = extensionManager.getGuiController("pluginID");
	// if (guiController == null) {
	// LOGGER.error("Could not find any plugin to render the URL");
	// return new ModelAndView(new RedirectView(UI_404_HTML));
	// }
	// final PluginView renderDelete = guiController.renderDelete(_request, _response);
	// return new ModelAndView(renderDelete.getView(), renderDelete.getModel());
	//
	// }
	//
	// @RequestMapping(value = "/ui/{pluginID}/*", method = RequestMethod.GET)
	// public ModelAndView resolveGet(@PathVariable final String pluginID, final HttpServletRequest _request,
	// final HttpServletResponse _response) {
	// final IPluginGuiControllerExtension guiController = extensionManager.getGuiController("pluginID");
	// if (guiController == null) {
	// LOGGER.error("Could not find any plugin to render the URL");
	// return new ModelAndView(new RedirectView(UI_404_HTML));
	// }
	// final PluginView renderGet = guiController.renderGet(_request, _response);
	// return new ModelAndView(renderGet.getView(), renderGet.getModel());
	//
	// }
	//
	// @RequestMapping(value = "/ui/{pluginID}/*", method = RequestMethod.POST)
	// public ModelAndView resolvePost(@PathVariable final String pluginID, final HttpServletRequest _request,
	// final HttpServletResponse _response, final BindingResult _result) {
	// final IPluginGuiControllerExtension guiController = extensionManager.getGuiController("pluginID");
	// if (guiController == null) {
	// LOGGER.error("Could not find any plugin to render the URL");
	// return new ModelAndView(new RedirectView(UI_404_HTML));
	// }
	// final PluginView renderPost = guiController.renderPost(_request, _response);
	// return new ModelAndView(renderPost.getView(), renderPost.getModel());
	//
	// }
}
