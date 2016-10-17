package fr.echoes.labs.komea.foundation.plugins.jenkins.extensions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.utils.ProjectDtoFactory;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.IJenkinsService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsBuildInfo;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsConfigurationService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsErrorHandlingService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsNameResolver;
import fr.echoes.labs.komea.foundation.plugins.jenkins.utils.JenkinsConstants;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.plugins.utils.ThymeleafTemplateEngineUtils;

/**
 * @author dcollard
 *
 */
@Component
public class JenkinsProjectDashboardWidget implements ProjectDashboardWidget {

    private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();

    private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsProjectDashboardWidget.class);

    @Autowired
    private JenkinsConfigurationService configurationService;

    @Autowired
    private IJenkinsService jenkinsService;
    
    @Autowired
    private JenkinsNameResolver nameResolver;

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

        final Project project = this.projectDAO.findOne(projectId);
        final ProjectDto projectDto = ProjectDtoFactory.convert(project);

        final WebContext ctx = new WebContext(this.request, this.response, this.servletContext);
        ctx.setVariable("projectId", projectId);

        try {
            final List<JenkinsBuildInfo> buildInfo = this.jenkinsService.getBuildInfo(projectDto);

            final String baseUrl = getBaseUrl();

            ctx.setVariable("buildBase", baseUrl + "/ui/projects/" + project.getKey() + "?buildUrl=");

            ctx.setVariable("jenkinsBuildHistory", buildInfo);
        } catch (final JenkinsExtensionException e) {
            LOGGER.error("[Jenkins] Failed to retrieve build history", e);
            this.errorHandler.registerError(e);
        }

        ctx.setVariable("jenkinsError", this.errorHandler.retrieveError());

        return templateEngine.process("jenkinsPanel", ctx);
    }

    private String getBaseUrl() {
        return this.request.getContextPath();
    }

    @Override
    public String getIconUrl() {
        return "/pictures/jenkins.png";
    }

    @Override
    public String getTitle() {
        return new MessageSourceAccessor(this.messageResource).getMessage("foundation.jenkins");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {

        final IProjectTabPanel iframePanel = new IProjectTabPanel() {

            @Override
            public String getTitle() {
                return new MessageSourceAccessor(messageResource).getMessage("foundation.jenkins.tab.title");
            }

            @Override
            public String getContent() {

                final Context ctx = new Context();

                final HttpServletRequest request
                        = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

                String url = configurationService.getUrl();

                final String buildUrl = request.getParameter("buildUrl");

                if (StringUtils.isNotEmpty(buildUrl)) {
                    try {
                        url = URLDecoder.decode(buildUrl, "UTF-8");
                    } catch (final UnsupportedEncodingException e) {
                        LOGGER.error("[jenkins]", e);
                        url = configurationService.getUrl();
                    }
                } else {
                    try {
                        final Project project = projectDao.findByKey(projectKey);
                        final String jobName = nameResolver.getFolderJobName(project);
                        final String jobId = jenkinsService.getJobId(jobName);
                        if (StringUtils.isNotEmpty(jobId)) {
                            url = url + "/job/" + jobId;
                        }
                    } catch (final JenkinsExtensionException e) {
                        LOGGER.error("[Jenkins] failed to construct Jenkins project URL", e);
                    }
                }

                LOGGER.info("[jenkins] project URL : {}", url);

                ctx.setVariable("jenkinsURL", url);

                return templateEngine.process("jenkinsManagementPanel", ctx);
            }

            @Override
            public String getIconUrl() {
                return JenkinsProjectDashboardWidget.this.getIconUrl();
            }

            @Override
            public String getId() {
                return JenkinsConstants.ID;
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
        return JenkinsConstants.ID;
    }
}
