package fr.echoes.labs.ksf.cc.plugins.dashboard.extensions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.dashboard.DashboardConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardLiferayService;
import fr.echoes.labs.ksf.cc.plugins.dashboard.utils.DashboardConstants;
import fr.echoes.labs.ksf.cc.plugins.dashboard.utils.DashboardUrlBuilder;
import fr.echoes.labs.ksf.plugins.utils.ThymeleafTemplateEngine;

/**
 * @author dcollard
 *
 */
@Component
public class DashboardProjectDashboardWidget implements ProjectDashboardWidget {

    private static ThymeleafTemplateEngine templateEngine = new ThymeleafTemplateEngine();

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardProjectDashboardWidget.class);

    @Autowired
    private DashboardConfigurationService configurationService;

    @Autowired
    private DashboardLiferayService liferayService;

    @Autowired
    private IProjectDAO projectDao;

    @Autowired
    private MessageSource messageResource;

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
        return new MessageSourceAccessor(this.messageResource).getMessage("foundation.dashboard");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {

    	final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
        final Project project = this.projectDao.findByKey(projectKey);

        final IProjectTabPanel iframePanel = new IProjectTabPanel() {

            @Override
            public String getTitle() {
                return new MessageSourceAccessor(DashboardProjectDashboardWidget.this.messageResource).getMessage("foundation.dashboard.tab.title");
            }

            @Override
            public String getContent() {
	                
        		final Context ctx = new Context();
                String url = configuration.getUrl();

                if (project != null) {

                    final String projectDashboardKey = liferayService.getLiferaySiteName(project);

                    url = new DashboardUrlBuilder()
                            .setBaseUrl(configuration.getUrl())
                            .setProjectKey(projectDashboardKey)
                            .build();

                    ctx.setVariable("projectDashboardKey", projectDashboardKey);
                }

                LOGGER.info("[dashboard] project URL : {}", url);
                ctx.setVariable("dashboardURL", url);
                
                return templateEngine.process("dashboardManagementPanel", ctx, this.getClass().getClassLoader());
            }

            @Override
            public String getIconUrl() {
                return DashboardProjectDashboardWidget.this.getIconUrl();
            }

            @Override
            public String getId() {
                return DashboardConstants.ID;
            }
        };

        return Lists.newArrayList(iframePanel);
    }

    @Override
    public boolean hasHtmlPanelBody() {
        return false;
    }

    @Override
    public String getId() {
        return DashboardConstants.ID;
    }

}
