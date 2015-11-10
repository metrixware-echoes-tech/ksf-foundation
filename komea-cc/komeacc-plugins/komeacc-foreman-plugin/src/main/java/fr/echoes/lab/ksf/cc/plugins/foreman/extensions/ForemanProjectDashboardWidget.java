package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.util.ArrayList;
import java.util.List;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.lab.ksf.plugins.utils.HtmlTemplateClasspathLoaderService;

public class ForemanProjectDashboardWidget implements ProjectDashboardWidget {

	@Override
	public List<MenuAction> getDropdownActions() {
		final ArrayList<MenuAction> arrayList = new ArrayList<>();

		return arrayList;
	}

	@Override
	public String getHtmlPanelBody() {
		return new HtmlTemplateClasspathLoaderService().getHtmlContent(Thread.currentThread().getContextClassLoader(),
				"templates/foremanPanel.html");
	}

	@Override
	public String getIconUrl() {
		return "/pictures/foreman.png";
	}

	@Override
	public String getTitle() {
		return "Foreman";
	}

}
