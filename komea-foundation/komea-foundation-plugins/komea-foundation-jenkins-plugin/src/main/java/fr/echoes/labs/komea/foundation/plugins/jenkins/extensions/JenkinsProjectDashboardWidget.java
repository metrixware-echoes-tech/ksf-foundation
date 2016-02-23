package fr.echoes.labs.komea.foundation.plugins.jenkins.extensions;

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

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.IJenkinsService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsBuildInfo;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsConfigurationService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsErrorHandlingService;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;

/**
 * @author dcollard
 *
 */
@Component
public class JenkinsProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = createTemplateEngine();

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsProjectDashboardWidget.class);

	@Autowired
	private JenkinsConfigurationService configurationService;

	@Autowired
	private IJenkinsService jenkinsService;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private JenkinsErrorHandlingService errorHandler;

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
			final List<JenkinsBuildInfo> buildInfo = this.jenkinsService.getBuildInfo(projectName);

			ctx.setVariable("jenkinsBuildHistory", buildInfo);
		} catch (final JenkinsExtensionException e) {
			LOGGER.error("[Jenkins] Failed to retrieve build history", e);
			this.errorHandler.registerError(e);
		}

		ctx.setVariable("jenkinsError", this.errorHandler.retrieveError());

		return templateEngine.process("jenkinsPanel", ctx);
	}

	@Override
	public String getIconUrl() {
		return "/pictures/jenkins.png";
	}

	@Override
	public String getTitle() {
		return "Jenkins";
	}


	@Override
	public List<IProjectTabPanel> getTabPanels() {

		final IProjectTabPanel iframePanel = new IProjectTabPanel() {

			@Override
			public String getTitle() {
				return "Continuous Integration (Jenkins)";
			}

			@Override
			public String getContent() {

				final Context ctx = new Context();

				final HttpServletRequest request =
						((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
						.getRequest();

				final String url = JenkinsProjectDashboardWidget.this.configurationService.getUrl();

				ctx.setVariable("jenkinsURL", url);

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
