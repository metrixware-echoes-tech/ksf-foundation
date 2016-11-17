package fr.echoes.labs.komea.foundation.plugins.git.extensions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.WebContext;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.utils.ProjectDtoFactory;

import fr.echoes.labs.komea.foundation.plugins.git.GitPlugin;
import fr.echoes.labs.komea.foundation.plugins.git.services.GitNameResolver;
import fr.echoes.labs.ksf.cc.extensions.gui.KomeaFoundationContext;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.extensions.services.ErrorHandlingService;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.plugins.utils.ThymeleafTemplateEngine;
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;

/**
 * @author dcollard
 *
 */
@Component
public class GitProjectDashboardWidget implements ProjectDashboardWidget {

    private static ThymeleafTemplateEngine templateEngine = new ThymeleafTemplateEngine();

    private static final Logger LOGGER = LoggerFactory.getLogger(GitProjectDashboardWidget.class);

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private ErrorHandlingService errorHandler;

    @Autowired
    private KomeaFoundationContext foundation;

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

        final WebContext ctx = new WebContext(this.foundation.getRequest(), this.foundation.getResponse(), this.foundation.getServletContext());

        ctx.setVariable("projectId", projectId);
        ctx.setVariable("gitRepoUrl", this.nameResolver.getProjectScmUrl(projectDto, logginName));
        ctx.setVariable("gitError", this.errorHandler.retrieveError(GitPlugin.ID));
        ctx.setVariable("gitMergeError", this.errorHandler.retrieveError(GitPlugin.MERGE_ERROR));
        ctx.setVariable("copyToClipboard", new MessageSourceAccessor(this.messageResource).getMessage("foundation.git.copyToClipboard"));

        return templateEngine.process("gitPanel", ctx, this.getClass().getClassLoader());
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
        return GitPlugin.ID;
    }

}
