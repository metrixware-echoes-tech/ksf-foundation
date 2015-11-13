package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.common.collect.Lists;

import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.lab.ksf.cc.plugins.foreman.utils.ThymeleafTemplateEngineUtils;

public class ForemanProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();
		
	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectDashboardWidget.class);
	
	private String foremanURL;
	
	public ForemanProjectDashboardWidget(String foremanURL) {
		this.foremanURL = foremanURL;
	}
	
	@Override
	public List<MenuAction> getDropdownActions() {
		
		MenuAction actionCreateTarget = new MenuAction();
		actionCreateTarget.setActionName("Create Target");
		actionCreateTarget.setUrl("javascript:displayCreateTargetModal()");
		
		MenuAction actionCreateEnv = new MenuAction();
		actionCreateEnv.setActionName("Create Environment");
		actionCreateEnv.setUrl("javascript:displayCreateEnvModal()");
		
		return Lists.newArrayList(actionCreateTarget, actionCreateEnv);
		
	}

	@Override
	public String getHtmlPanelBody() {
		
		LOGGER.info("[foreman-plugin] foreman url: {}", foremanURL);
		
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
