package fr.echoes.labs.ksf.cc.plugins.redmine.extensions;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineErrorHandlingService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineService;

/**
 * @author dcollard
 *
 */
@Component
public class RedmineProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = createTemplateEngine();

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectDashboardWidget.class);

	@Autowired
	private RedmineConfigurationService configurationService;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private RedmineErrorHandlingService errorHandler;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private ServletContext servletContext;

	@Override
	public List<MenuAction> getDropdownActions() {

		return null;
	}

	@Override
	public String getHtmlPanelBody(String projectId) {

		final Project project = this.projectDAO.findOne(projectId);

		final WebContext ctx = new WebContext(this.request, this.response, this.servletContext);
		ctx.setVariable("projectId", projectId);

		final String projectName = project.getName();

		try {

			final IRedmineService redmine = new RedmineService();

			final RedmineQuery query = new RedmineQuery.Builder().projectName(projectName).build();

			final List<RedmineIssue> issues = redmine.queryIssues(query);

			ctx.setVariable("issues", issues);

		} catch (final Exception e) {
			LOGGER.error("[Redmine] Failed to list issues for project: " + projectName, e);
			this.errorHandler.registerError(e.getMessage());
		}

		ctx.setVariable("redmineError", this.errorHandler.retrieveError());

		return templateEngine.process("redminePanel", ctx);
	}

	@Override
	public String getIconUrl() {
		return "/pictures/redmine.png";
	}

	@Override
	public String getTitle() {
		return "Redmine";
	}


	@Override
	public List<IProjectTabPanel> getTabPanels() {

		final IProjectTabPanel iframePanel = new IProjectTabPanel() {

			@Override
			public String getTitle() {
				return "Forge (Redmine)";
			}

			@Override
			public String getContent() {

				final Context ctx = new Context();

				final HttpServletRequest request =
						((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
						.getRequest();

				final String url = RedmineProjectDashboardWidget.this.configurationService.getUrl();

				ctx.setVariable("redmineURL", url);

				return templateEngine.process("managementPanel", ctx);
			}
		};

		return Lists.newArrayList(iframePanel);
	}


	private static TemplateEngine createTemplateEngine() {

		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("XHTML");
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");

		final TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		return templateEngine;

	}

}
