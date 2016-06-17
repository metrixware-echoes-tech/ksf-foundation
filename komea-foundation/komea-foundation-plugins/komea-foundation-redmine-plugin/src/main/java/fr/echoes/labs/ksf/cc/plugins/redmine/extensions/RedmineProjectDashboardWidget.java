package fr.echoes.labs.ksf.cc.plugins.redmine.extensions;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery.Builder;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineErrorHandlingService;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

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

    @Autowired
    private IRedmineService redmineService;

    @Autowired
    IProjectDAO projectDao;

    @Autowired
    private MessageSource messageResource;

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

            final Builder redmineQuerryBuilder = new RedmineQuery.Builder();
            redmineQuerryBuilder.projectName(projectName);
            redmineQuerryBuilder.resultItemsLimit(this.configurationService.getResultItemsLimit());

            final RedmineQuery query = redmineQuerryBuilder.build();

            final List<RedmineIssue> issues = this.redmineService.queryIssues(query);

            final MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(this.messageResource);

            ctx.setVariable("foundationRedmineWidgetTargetVersion", messageSourceAccessor.getMessage("foundation.redmine.widget.targetVersion"));
            ctx.setVariable("foundationRedmineWidgetStatus", messageSourceAccessor.getMessage("foundation.redmine.widget.status"));
            ctx.setVariable("foundationRedmineWidgetPriority", messageSourceAccessor.getMessage("foundation.redmine.widget.priority"));
            ctx.setVariable("foundationRedmineWidgetSubject", messageSourceAccessor.getMessage("foundation.redmine.widget.subject"));
            ctx.setVariable("foundationRedmineWidgetAssignedTo", messageSourceAccessor.getMessage("foundation.redmine.widget.assignedTo"));

            final String baseUrl = getBaseUrl(this.request);

            ctx.setVariable("issuesBase", baseUrl + "/ui/projects/" + project.getKey() + "?redmineIssue=");

            ctx.setVariable("issues", issues);

        } catch (final Exception e) {
            LOGGER.error("[Redmine] Failed to list issues for project: " + projectName, e);
            this.errorHandler.registerError(e.getMessage());
        }

        ctx.setVariable("redmineError", this.errorHandler.retrieveError());

        return templateEngine.process("redminePanel", ctx);
    }

    private String getBaseUrl(HttpServletRequest request) {
        return request.getContextPath();
    }

    @Override
    public String getIconUrl() {
        return "/pictures/komea-activity.png";
    }

    @Override
    public String getTitle() {
        return new MessageSourceAccessor(this.messageResource).getMessage("foundation.redmine");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {

        final IProjectTabPanel iframePanel = new IProjectTabPanel() {

            @Override
            public String getTitle() {

                return new MessageSourceAccessor(RedmineProjectDashboardWidget.this.messageResource).getMessage("foundation.redmine.tab.title");
            }

            @Override
            public String getContent() {

                final Context ctx = new Context();

                final HttpServletRequest request
                        = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

                String url = RedmineProjectDashboardWidget.this.configurationService.getUrl();

                final String redmineIssue = request.getParameter("redmineIssue");

                if (StringUtils.isNotEmpty(redmineIssue)) {
                    LOGGER.info("[redmine] paramater redmineIssue={} found in URL.", redmineIssue);
                    url += "/issues/" + redmineIssue;
                } else {
                    final Project project = RedmineProjectDashboardWidget.this.projectDao.findByKey(projectKey);
                    if (project != null) {
                        try {
                            final String projectId = RedmineProjectDashboardWidget.this.redmineService.getProjectId(project.getName());
                            if (StringUtils.isNotEmpty(projectId)) {
                                url += "/projects/" + projectId;
                            }
                        } catch (final RedmineExtensionException e) {
                            LOGGER.error("[Redmine] failed to construct Redmine project URL", e);
                        }
                    }
                }

                LOGGER.info("[redmine] project URL : {}", url);

                ctx.setVariable("redmineURL", url);

                return templateEngine.process("redmineManagementPanel", ctx);
            }

            @Override
            public String getIconUrl() {
                return RedmineProjectDashboardWidget.this.getIconUrl();
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

    @Override
    public boolean hasHtmlPanelBody() {
        return true;
    }

}
