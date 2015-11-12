package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.lab.ksf.cc.plugins.foreman.utils.ThymeleafTemplateEngineUtils;

public class ForemanProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();
	
	@Override
	public List<MenuAction> getDropdownActions() {
		final ArrayList<MenuAction> arrayList = new ArrayList<>();

		return arrayList;
	}

	@Override
	public String getHtmlPanelBody() {
				
		Context ctx = new Context();
		ctx.setVariable("test", "test");
	
		return templateEngine.process("foremanPanel", ctx);		
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
