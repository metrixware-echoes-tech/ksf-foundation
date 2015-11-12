package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.common.collect.Lists;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.lab.ksf.cc.plugins.foreman.utils.ThymeleafTemplateEngineUtils;

public class ForemanProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();
	
	@Override
	public List<MenuAction> getDropdownActions() {
		
		MenuAction actionCreateTarget = new MenuAction();
		actionCreateTarget.setActionName("Create Target");
		actionCreateTarget.setUrl("/foreman/targets/new");
		
		MenuAction actionCreateEnv = new MenuAction();
		actionCreateEnv.setActionName("Create Environment");
		actionCreateEnv.setUrl("/foreman/environment/new");
		
		return Lists.newArrayList(actionCreateTarget, actionCreateEnv);
		
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

	@Override
	public List<IProjectTabPanel> getTabPanels() {
	
		IProjectTabPanel iframePanel = new IProjectTabPanel() {
			
			@Override
			public String getTitle() {
				return "Systems Management (Foreman)";
			}
			
			@Override
			public String getContent() {
				return templateEngine.process("managementPanel", new Context());
			}
		};
		
		return Lists.newArrayList(iframePanel);
	}

}
