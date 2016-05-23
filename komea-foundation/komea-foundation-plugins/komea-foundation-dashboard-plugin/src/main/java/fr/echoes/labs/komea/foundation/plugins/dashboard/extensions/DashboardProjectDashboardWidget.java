package fr.echoes.labs.komea.foundation.plugins.dashboard.extensions;

import java.text.Normalizer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardConfigurationService;


/**
 * @author dcollard
 *
 */
@Component
public class DashboardProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = createTemplateEngine();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardProjectDashboardWidget.class);

	@Autowired
	private DashboardConfigurationService configurationService;

	@Autowired
	IProjectDAO projectDao;	
	
	@Override
	public List<MenuAction> getDropdownActions() {
		return null;
	}

	@Override
	public String getHtmlPanelBody(String projectId) {;
		return null;
	}

	@Override
	public String getIconUrl() {
		return "/pictures/dashboard.png";
	}

	@Override
	public String getTitle() {
		return "Dashboard";
	}


	@Override
	public List<IProjectTabPanel> getTabPanels(String projectKey) {

		final Project project = this.projectDao.findByKey(projectKey);

		
		final IProjectTabPanel iframePanel = new IProjectTabPanel() {

			@Override
			public String getTitle() {
				return DashboardProjectDashboardWidget.this.getTitle();
			}

			@Override
			public String getContent() {

				final Context ctx = new Context();

				final HttpServletRequest request =
						((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
						.getRequest();

				String url = DashboardProjectDashboardWidget.this.configurationService.getUrl();

				if (project != null) {
					String projectId = createIdentifier(project.getName());
					if (!url.endsWith("/")) {
						url = url + '/';
					}
					url = url + "#list_entities=" + projectId;
				}
				
				
				ctx.setVariable("dashboardURL", url);

				return templateEngine.process("dashboardManagementPanel", ctx);
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
	
	private String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+","-" ).toLowerCase();
	}	

}
