package fr.echoes.labs.ksf.cc.plugins.foreman.extensions;

import com.tocea.corolla.products.dao.IProjectDAO;
import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanclient.IForemanService;
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanClientFactory;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;
import fr.echoes.labs.ksf.cc.plugins.foreman.utils.ForemanConstants;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Extension
public class ForemanProjectLifeCycleExtension implements IProjectLifecycleExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectLifeCycleExtension.class);

    @Autowired
    private IForemanService foremanService;

    @Autowired
    private ForemanClientFactory foremanClientFactory;

    @Autowired
    private ForemanErrorHandlingService errorHandler;

    @Autowired
    private IForemanTargetDAO targetDAO;

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public NotifyResult notifyCreatedProject(ProjectDto _project) {

        final String logginName = this.currentUserService.getCurrentUserLogin();
        //SecurityContextHolder.getContext().getAuthentication().getName();

        if (StringUtils.isEmpty(logginName)) {
            LOGGER.error("[Foreman] No user found. Aborting project creation in Foreman module");
            return NotifyResult.CONTINUE;
        }

        LOGGER.info("[Foreman] project {} creation detected [demanded by: {}]", _project.getKey(), logginName);

        try {

            // Create the project in Foreman
            final IForemanApi foremanApi = this.foremanClientFactory.createForemanClient();
            this.foremanService.createProject(foremanApi, _project.getName(), logginName);

        } catch (final Exception e) {
            LOGGER.error("[Foreman] project creation failed", e);
            this.errorHandler.registerError("Unable to create Foreman project. Please verify your Foreman configuration.");
        }
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyDeletedProject(ProjectDto _project) {

        try {

            // Delete targets associated to the project
            final List<ForemanTarget> targets = this.targetDAO.findByProject(this.projectDAO.findOne(_project.getId()));
            this.targetDAO.delete(targets);

            // Delete project data in Foreman
            final IForemanApi foremanApi = this.foremanClientFactory.createForemanClient();
            this.foremanService.deleteProject(foremanApi, _project.getName());

        } catch (final Exception ex) {
            LOGGER.error("[foreman] project delete failed", ex);
            this.errorHandler.registerError("Unable to delete Foreman project. Please verify your Foreman configuration.");
        }
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyDuplicatedProject(ProjectDto _project) {
        // Do nothing
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyUpdatedProject(ProjectDto _project) {
        // Do nothing
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyCreatedRelease(ProjectDto project, String releaseVersion, String username) {
        // Do nothing
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyCreatedFeature(ProjectDto project, String featureId,
            String featureSubject, String username) {
        // Do nothing
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyFinishedRelease(ProjectDto project, String releaseName) {
        // Do nothing
        return NotifyResult.CONTINUE;
    }

    @Override
    public NotifyResult notifyFinishedFeature(ProjectDto projectDto, String featureId,
            String featureSubject) {
        // Do nothing
        return NotifyResult.CONTINUE;

    }

    @Override
    public NotifyResult notifyCanceledFeature(ProjectDto projectDto, String featureId,
            String featureSubject) {
        // Do nothing
        return NotifyResult.CONTINUE;

    }

    @Override
    public String getName() {
        return ForemanConstants.ID;
    }

}
