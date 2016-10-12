package fr.echoes.labs.komea.foundation.plugins.git.extensions;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.utils.ProjectDtoFactory;

import fr.echoes.labs.komea.foundation.plugins.git.services.GitErrorHandlingService;
import fr.echoes.labs.komea.foundation.plugins.git.services.GitNameResolver;
import fr.echoes.labs.komea.foundation.plugins.git.utils.GitConstants;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.plugins.utils.ThymeleafTemplateEngineUtils;
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;

/**
 * @author dcollard
 *
 */
@Component
public class GitProjectDashboardWidget implements ProjectDashboardWidget {

    private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();

    private static final Logger LOGGER = LoggerFactory.getLogger(GitProjectDashboardWidget.class);

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

    @Autowired
    private MessageSource messageResource;
    
    @Autowired
    private GitNameResolver nameResolver;
    
    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public List<MenuAction> getDropdownActions() {
        return null;
    }

    @Override
    public String getHtmlPanelBody(String projectId) {

        final Project project = this.projectDAO.findOne(projectId);
        
        final ProjectDto projectDto = ProjectDtoFactory.convert(project);
        final String logginName = this.currentUserService.getCurrentUserLogin();

        final WebContext ctx = new WebContext(this.request, this.response, this.servletContext);

        ctx.setVariable("projectId", projectId);
        ctx.setVariable("gitRepoUrl", this.nameResolver.getProjectScmUrl(projectDto, logginName));
        ctx.setVariable("gitError", this.errorHandler.retrieveError());
        ctx.setVariable("gitMergeError", this.errorHandler.retrieveError(GitErrorHandlingService.SESSION_ITEM_GIT_MERGE_ERROR));
        ctx.setVariable("copyToClipboard", new MessageSourceAccessor(this.messageResource).getMessage("foundation.git.copyToClipboard"));

        return templateEngine.process("gitPanel", ctx);
    }

    @Override
    public String getIconUrl() {
        return "/pictures/git.png";
    }

    @Override
    public String getTitle() {
        return new MessageSourceAccessor(this.messageResource).getMessage("foundation.git");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {
        return Lists.newArrayList();
    }

    @Override
    public boolean hasHtmlPanelBody() {
        return true;
    }

    @Override
    public String getId() {
        return GitConstants.ID;
    }

}
