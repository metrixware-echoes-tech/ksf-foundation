package fr.echoes.labs.ksf.cc.extmanager.ui;

import fr.echoes.labs.ksf.cc.extensions.gui.IPluginGuiControllerExtension;

public interface IPluginMVCExtensionManager {
	
	IPluginGuiControllerExtension getGuiController(String _pluginGuiExtension);
	
}