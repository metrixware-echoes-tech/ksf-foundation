package fr.echoes.labs.ksf.cc.plugins.foreman.extensions;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanclient.ForemanClient;
import fr.echoes.labs.foremanclient.ForemanService;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanEnvironmentDAO;
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;
import fr.echoes.labs.ksf.cc.plugins.foreman.utils.ThymeleafTemplateEngineUtils;

@Component
public class ForemanProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectDashboardWidget.class);

	@Autowired
	private ForemanConfigurationService configurationService;

	@Autowired
	private IForemanEnvironmentDAO environmentDAO;

	@Autowired
	private IForemanTargetDAO targetDAO;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private ForemanErrorHandlingService errorHandler;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private ServletContext servletContext;

	@Override
	public List<MenuAction> getDropdownActions() {

		final MenuAction actionCreateEnv = new MenuAction();
		actionCreateEnv.setActionName("Create Environment");
		actionCreateEnv.setUrl("javascript:displayCreateEnvModal()");

		final MenuAction actionCreateTarget = new MenuAction();
		actionCreateTarget.setActionName("Create Target");
		actionCreateTarget.setUrl("javascript:displayCreateTargetModal()");

		return Lists.newArrayList(actionCreateEnv, actionCreateTarget);

	}

	@Override
	public String getHtmlPanelBody(String projectId) {

		final Project project = this.projectDAO.findOne(projectId);

		final WebContext ctx = new WebContext(this.request, this.response, this.servletContext);
		ctx.setVariable("projectId", projectId);

		final List<ForemanEnvironnment> environments = this.environmentDAO.findAll();
		ctx.setVariable("environments", environments);

		final Iterable<ForemanTarget> projectTargets = this.targetDAO.findByProject(project);
		ctx.setVariable("targets", projectTargets);

		try {

			final IForemanApi foremanApi = ForemanClient.createApi(this.configurationService.getForemanUrl(), this.configurationService.getForemanUsername(), this.configurationService.getForemanPassword());

			ctx.setVariable("operatingSystems", Lists.newArrayList(foremanApi.getOperatingSystems(null, null, null, ForemanService.PER_PAGE_RESULT).results));

			ctx.setVariable("computeProfiles", Lists.newArrayList(foremanApi.getComputeProfiles(null, null, null, ForemanService.PER_PAGE_RESULT).results));

		} catch (final Exception e) {
			LOGGER.error("[foreman] Foreman API call failed : {}", e);
			this.errorHandler.registerError("Unable to invoke Foreman. Please verify your Foreman configuration");
		}

		ctx.setVariable("foremanError", this.errorHandler.retrieveError());

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
	public List<IProjectTabPanel> getTabPanels(final String projectKey) {

		final IProjectTabPanel iframePanel = new IProjectTabPanel() {

			@Override
			public String getTitle() {
				return "Systems Management (Foreman)";
			}

			@Override
			public String getContent() {

				final Context ctx = new Context();

				final HttpServletRequest request =
						((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
						.getRequest();

				String foremanURL = ForemanProjectDashboardWidget.this.configurationService.getForemanUrl();

				final String foremanHost = request.getParameter("foremanHost");

				if (StringUtils.isNotEmpty(foremanHost)) {
					foremanURL += "/hosts/"+foremanHost;
				}

				ctx.setVariable("foremanURL", foremanURL);

				return templateEngine.process("foremanManagementPanel", ctx);
			}

			@Override
			public String getIconUrl() {
				return ForemanProjectDashboardWidget.this.getIconUrl();
			}
		};

		return Lists.newArrayList(iframePanel);
	}

}
