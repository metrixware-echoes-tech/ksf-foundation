package fr.echoes.labs.ksf.cc.plugins.redmine.extensions;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.utils.ProjectDtoFactory;

import fr.echoes.labs.ksf.cc.extensions.gui.KomeaFoundationContext;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.extensions.services.ErrorHandlingService;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedminePlugin;
import fr.echoes.labs.ksf.cc.plugins.redmine.model.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.redmine.utils.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.utils.RedmineQuery.Builder;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.plugins.utils.ThymeleafTemplateEngine;

/**
 * @author dcollard
 *
 */
@Component
public class RedmineProjectDashboardWidget implements ProjectDashboardWidget {

    private static ThymeleafTemplateEngine templateEngine = new ThymeleafTemplateEngine();

    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectDashboardWidget.class);

    @Autowired
    private RedmineConfigurationService configurationService;

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private ErrorHandlingService errorHandler;

    @Autowired
    private KomeaFoundationContext foundation;

    @Autowired
    private IRedmineService redmineService;

    @Autowired
    private IProjectDAO projectDao;

    @Autowired
    private MessageSource messageResource;

    @Override
    public List<MenuAction> getDropdownActions() {
        return null;
    }

    @Override
    public String getHtmlPanelBody(String projectId) {

    	final RedmineConfigurationBean configuration = this.configurationService.getConfigurationBean();
        final Project project = this.projectDAO.findOne(projectId);
        final WebContext ctx = this.foundation.newThymeleafWebContext();;
        ctx.setVariable("projectId", projectId);
        
        final ProjectDto projectDto = ProjectDtoFactory.convert(project);
    	final String projectKey = this.redmineService.getProjectId(projectDto);

        try {
        	
            final Builder redmineQuerryBuilder = new RedmineQuery.Builder();
            redmineQuerryBuilder.projectKey(projectKey);
            redmineQuerryBuilder.resultItemsLimit(configuration.getResultItemsLimit());

            final RedmineQuery query = redmineQuerryBuilder.build();

            final List<RedmineIssue> issues = this.redmineService.queryIssues(query);

            final MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(this.messageResource);

            ctx.setVariable("foundationRedmineWidgetTargetVersion", messageSourceAccessor.getMessage("foundation.redmine.widget.targetVersion"));
            ctx.setVariable("foundationRedmineWidgetStatus", messageSourceAccessor.getMessage("foundation.redmine.widget.status"));
            ctx.setVariable("foundationRedmineWidgetPriority", messageSourceAccessor.getMessage("foundation.redmine.widget.priority"));
            ctx.setVariable("foundationRedmineWidgetSubject", messageSourceAccessor.getMessage("foundation.redmine.widget.subject"));
            ctx.setVariable("foundationRedmineWidgetAssignedTo", messageSourceAccessor.getMessage("foundation.redmine.widget.assignedTo"));

            final String baseUrl = this.foundation.getContextPath();

            ctx.setVariable("issuesBase", baseUrl + "/ui/projects/" + project.getKey() + "?redmineIssue=");

            ctx.setVariable("issues", issues);

        } catch (final Exception e) {
            LOGGER.error("[Redmine] Failed to list issues for project: " + projectKey, e);
            this.errorHandler.registerError(RedminePlugin.ID, e.getMessage());
        }

        ctx.setVariable("redmineError", this.errorHandler.retrieveError(RedminePlugin.ID));

        return templateEngine.process("redminePanel", ctx, this.getClass().getClassLoader());
    }

    @Override
    public String getIconUrl() {
    	if (this.configurationService.getConfigurationBean().getKomeaActivity()) {
    		return RedminePlugin.ICON_KOMEA_ACTIVITY;
    	}
    	return RedminePlugin.ICON_REDMINE;
    }

    @Override
    public String getTitle() {
    	final MessageSourceAccessor accessor = new MessageSourceAccessor(this.messageResource);
    	if (this.configurationService.getConfigurationBean().getKomeaActivity()) {
    		return accessor.getMessage("foundation.komeaActivity");
    	}
        return accessor.getMessage("foundation.redmine");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {

    	final RedmineConfigurationBean configuration = this.configurationService.getConfigurationBean();
    	
        final IProjectTabPanel iframePanel = new IProjectTabPanel() {

            @Override
            public String getTitle() {
            	final MessageSourceAccessor accessor = new MessageSourceAccessor(RedmineProjectDashboardWidget.this.messageResource);
            	if (configuration.getKomeaActivity()) {
            		return accessor.getMessage("foundation.komeaActivity.tab.title");
            	}
                return accessor.getMessage("foundation.redmine.tab.title");
            }

            @Override
            public String getContent() {

                final Context ctx = new Context();
                String url = configuration.getUrl();

                final String redmineIssue = foundation.getRequest().getParameter("redmineIssue");

                if (StringUtils.isNotEmpty(redmineIssue)) {
                    LOGGER.info("[redmine] paramater redmineIssue={} found in URL.", redmineIssue);
                    url += "/issues/" + redmineIssue;
                } else {
                    final Project project = RedmineProjectDashboardWidget.this.projectDao.findByKey(projectKey);
                    if (project != null) {
                    	final ProjectDto projectDto = ProjectDtoFactory.convert(project);
                        final String projectId = RedmineProjectDashboardWidget.this.redmineService.getProjectId(projectDto);
                        if (StringUtils.isNotEmpty(projectId)) {
                            url += "/projects/" + projectId;
                        }
                    }
                }

                LOGGER.info("[redmine] project URL : {}", url);

                ctx.setVariable("redmineURL", url);

                return templateEngine.process("redmineManagementPanel", ctx, this.getClass().getClassLoader());
            }

            @Override
            public String getIconUrl() {
                return RedmineProjectDashboardWidget.this.getIconUrl();
            }

            @Override
            public String getId() {
                return RedminePlugin.ID;
            }
        };

        return Lists.newArrayList(iframePanel);
    }

    @Override
    public boolean hasHtmlPanelBody() {
        return true;
    }

    @Override
    public String getId() {
        return RedminePlugin.ID;
    }

}
