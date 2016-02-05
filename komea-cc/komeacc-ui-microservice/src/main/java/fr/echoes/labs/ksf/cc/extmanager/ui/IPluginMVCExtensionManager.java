package fr.echoes.labs.ksf.cc.extmanager.ui;

import fr.echoes.lab.ksf.cc.extensions.gui.IPluginGuiControllerExtension;

public interface IPluginMVCExtensionManager {
	
	IPluginGuiControllerExtension getGuiController(String _pluginGuiExtension);
	
}