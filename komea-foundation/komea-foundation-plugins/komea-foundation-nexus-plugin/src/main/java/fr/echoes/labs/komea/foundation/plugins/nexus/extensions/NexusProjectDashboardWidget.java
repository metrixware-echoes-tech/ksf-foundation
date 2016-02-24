package fr.echoes.labs.komea.foundation.plugins.nexus.extensions;

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

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.nexus.services.NexusConfigurationService;

/**
 * @author dcollard
 *
 */
@Component
public class NexusProjectDashboardWidget implements ProjectDashboardWidget {

	private static TemplateEngine templateEngine = createTemplateEngine();


	private static final Logger LOGGER = LoggerFactory.getLogger(NexusProjectDashboardWidget.class);

	@Autowired
	private NexusConfigurationService configurationService;

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
		return "/pictures/nexus.png";
	}

	@Override
	public String getTitle() {
		return "Nexus";
	}


	@Override
	public List<IProjectTabPanel> getTabPanels() {
		final IProjectTabPanel iframePanel = new IProjectTabPanel() {

			@Override
			public String getTitle() {
				return "Repository Manager (" + NexusProjectDashboardWidget.this.getTitle() + ")";
			}

			@Override
			public String getContent() {

				final Context ctx = new Context();

				final HttpServletRequest request =
						((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
						.getRequest();

				final String url = NexusProjectDashboardWidget.this.configurationService.getUrl();

				ctx.setVariable("nexusURL", url);

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
