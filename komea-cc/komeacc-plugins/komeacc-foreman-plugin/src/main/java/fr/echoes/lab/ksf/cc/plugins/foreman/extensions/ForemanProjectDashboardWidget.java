package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.common.collect.Lists;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanclient.ForemanClient;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.lab.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanConfigurationService;
import fr.echoes.lab.ksf.cc.plugins.foreman.utils.ThymeleafTemplateEngineUtils;

public class ForemanProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectDashboardWidget.class);

	private final ForemanConfigurationService configurationService;


	public ForemanProjectDashboardWidget(
			ForemanConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public List<MenuAction> getDropdownActions() {

		final MenuAction actionCreateTarget = new MenuAction();
		actionCreateTarget.setActionName("Create Target");
		actionCreateTarget.setUrl("javascript:displayCreateTargetModal()");

		final MenuAction actionCreateEnv = new MenuAction();
		actionCreateEnv.setActionName("Create Environment");
		actionCreateEnv.setUrl("javascript:displayCreateEnvModal()");

		return Lists.newArrayList(actionCreateTarget, actionCreateEnv);

	}

	@Override
	public String getHtmlPanelBody() {

		final Context ctx = new Context();
		ctx.setVariable("test", "test");

		try {

			final IForemanApi foremanApi = ForemanClient.createApi(this.configurationService.getForemanUrl(), this.configurationService.getForemanUsername(), this.configurationService.getForemanPassword());

			ctx.setVariable("operatingSystems", Lists.newArrayList(foremanApi.getOperatingSystems().results));

			ctx.setVariable("computeProfiles", Lists.newArrayList(foremanApi.getComputeProfiles().results));

		} catch (final Exception e) {
			LOGGER.error("[foreman] Foreman API call failed : {}", e);
		}

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

		final IProjectTabPanel iframePanel = new IProjectTabPanel() {

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
