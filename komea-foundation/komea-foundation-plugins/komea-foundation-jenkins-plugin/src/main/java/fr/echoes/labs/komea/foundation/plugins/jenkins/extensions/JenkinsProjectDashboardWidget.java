package fr.echoes.labs.komea.foundation.plugins.jenkins.extensions;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.IJenkinsService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsBuildInfo;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsConfigurationService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsErrorHandlingService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.utils.JenkinsConstants;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author dcollard
 *
 */
@Component
public class JenkinsProjectDashboardWidget implements ProjectDashboardWidget {

    private static final String SLASH = "/";

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
            final List<JenkinsBuildInfo> buildInfo = this.jenkinsService.getBuildInfo(projectName);

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
        return new MessageSourceAccessor(JenkinsProjectDashboardWidget.this.messageResource).getMessage("foundation.jenkins");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {

        final IProjectTabPanel iframePanel = new IProjectTabPanel() {

            @Override
            public String getTitle() {
                return new MessageSourceAccessor(JenkinsProjectDashboardWidget.this.messageResource).getMessage("foundation.jenkins.tab.title");
            }

            @Override
            public String getContent() {

                final Context ctx = new Context();

                final HttpServletRequest request
                        = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

                String url = JenkinsProjectDashboardWidget.this.configurationService.getSsoUrl();

                final String buildUrl = request.getParameter("buildUrl");

                if (StringUtils.isNotEmpty(buildUrl)) {

                    try {
                        url = URLDecoder.decode(buildUrl, "UTF-8");
                        String buildPath = new URL(url).getPath(); // /jenkins/job/
                        if (StringUtils.isNotEmpty(buildPath)) {
                            String jenkinsUrl = JenkinsProjectDashboardWidget.this.configurationService.getSsoUrl();
                            String serviceBase = new URL(jenkinsUrl).getPath();

                            final int indexOfSecondSlash = serviceBase.indexOf(SLASH, serviceBase.indexOf(SLASH) + 1);
                            if (indexOfSecondSlash > 0) {
                                serviceBase = serviceBase.substring(0, indexOfSecondSlash);
                            }

                            final int indexOf = buildPath.indexOf(serviceBase);
                            if (indexOf == 0) {
                                buildPath = buildPath.substring(serviceBase.length());
                                if (jenkinsUrl.endsWith(SLASH)) {
                                    jenkinsUrl = jenkinsUrl.substring(0, jenkinsUrl.length() - 1);
                                }
                                url = jenkinsUrl + buildPath;
                            }
                        }

                    } catch (final UnsupportedEncodingException e) {
                        LOGGER.error("[jenkins]", e);
                        url = JenkinsProjectDashboardWidget.this.configurationService.getUrl();
                    } catch (final MalformedURLException e) {
                        LOGGER.error("[jenkins]", e);
                    }
                } else {
                    try {
                        final Project project = JenkinsProjectDashboardWidget.this.projectDao.findByKey(projectKey);
                        final String jobId = JenkinsProjectDashboardWidget.this.jenkinsService.getJobId(project.getName());
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

    @Override
    public String getId() {
        return JenkinsConstants.ID;
    }
}
