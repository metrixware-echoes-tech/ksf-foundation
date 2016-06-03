package fr.echoes.labs.komea.foundation.plugins.git.extensions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.komea.foundation.plugins.git.services.GitConfigurationService;
import fr.echoes.labs.komea.foundation.plugins.git.services.GitErrorHandlingService;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

/**
 * @author dcollard
 *
 */
@Component
public class GitProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = createTemplateEngine();

	private static final Logger LOGGER = LoggerFactory.getLogger(GitProjectDashboardWidget.class);

	@Autowired
	private GitConfigurationService config;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private GitErrorHandlingService errorHandler;

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

	private ICurrentUserService currentUserService;

	@Autowired
	private ApplicationContext applicationContext;

	public void init() {

		if (this.currentUserService == null) {
			this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
		}
	}


	@Override
	public String getHtmlPanelBody(String projectId) {

		final Project project = this.projectDAO.findOne(projectId);

		final WebContext ctx = new WebContext(this.request, this.response, this.servletContext);
		ctx.setVariable("projectId", projectId);

		final String projectName = project.getName();

		ctx.setVariable("gitRepoUrl", getProjectScmUrl(projectName));

		ctx.setVariable("gitError", this.errorHandler.retrieveError());

		return templateEngine.process("gitPanel", ctx);
	}

	@Override
	public String getIconUrl() {
		return "/pictures/git.png";
	}

	@Override
	public String getTitle() {
		return "Git";
	}


	@Override
	public List<IProjectTabPanel> getTabPanels(final String projectKey) {

		return Lists.newArrayList();
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

	private String getProjectScmUrl(String projectName) {
		init();
		final String logginName = this.currentUserService.getCurrentUserLogin();
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("scmUrl", this.config.getScmUrl());
		variables.put("projectName", projectName);
		variables.put("userLogin", logginName);
		return replaceVariables(this.config.getDisplayedUri(), variables);
	}

	private String replaceVariables(String str, Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

}
