package fr.echoes.labs.ksf.cc.extensions.gui;

import java.util.Map;

public class PluginView {
	String view;

	Map<String, Object> model;

	public Map<String, Object> getModel() {
		return model;
	}

	public String getView() {
		return view;
	}

	public void setModel(final Map<String, Object> _model) {
		model = _model;
	}

	public void setView(final String _view) {
		view = _view;
	}
}
