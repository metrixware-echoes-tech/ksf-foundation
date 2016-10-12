package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.utils.ProjectDtoFactory;

import fr.echoes.labs.ksf.cc.extensions.services.project.TicketStatus;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeatues;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersions;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.ProjectVersion;
import fr.echoes.labs.ksf.cc.releases.dao.IReleaseDAO;
import fr.echoes.labs.ksf.cc.releases.model.Release;
import fr.echoes.labs.ksf.cc.releases.model.ReleaseState;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;

/**
 * This service allows to obtain project information returned by the plug-ins.
 *
 *
 * @author dcollard
 *
 */
@Service("projectInformationManager")
public class ProjectInformationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectInformationManager.class);

    @Autowired(required = false)
    private IProjectVersions[] versions;

    @Autowired(required = false)
    private IProjectFeatues[] features;

    @Autowired
    private IReleaseDAO releaseDao;

    @Autowired
    private CurrentUserService currentUserService;

    /**
     * Returns the versions for this project.
     *
     * @param projectName the project name.
     * @return a list of versions.
     */
    public List<IProjectVersion> getVersions(Project project) {

        if (this.versions == null) {
            LOGGER.info("getVersions : no versions.");
            return Lists.newArrayList();
        }

        final ProjectDto projectDto = ProjectDtoFactory.convert(project);
        final List<IProjectVersion> result = Lists.newArrayList();
        final List<Release> startedReleases = this.releaseDao.findAll();

        for (final IProjectVersions projectRelease : this.versions) {
            final List<IProjectVersion> releases = projectRelease.getVersions(projectDto);
            if (releases != null) {
                for (final IProjectVersion r : releases) {
                    for (final Release startedRelease : startedReleases) {
                        if (StringUtils.equals(startedRelease.getReleaseId(), r.getId())) {
                            if (startedRelease.getState() == ReleaseState.FINISHED) {
                                ((ProjectVersion) r).setStatus(TicketStatus.FINISHED);
                            } else if (startedRelease.getState() == ReleaseState.STARTED) {
                                ((ProjectVersion) r).setStatus(TicketStatus.CREATED);
                            }
                            break;
                        }
                    }
                }
                result.addAll(releases);
            }
        }
        LOGGER.info("getVersions : the project \"{}\" has {} versions.", project.getName(), result.size());
        return result;
    }

    private boolean isStartedRelease(IProjectVersion release, List<Release> startedReleases) {
        for (final Release startedRelease : startedReleases) {
            if (StringUtils.equals(startedRelease.getReleaseId(), release.getId())) {
                return startedRelease.getState() != ReleaseState.FINISHED;
            }
        }
        return false;
    }

    /**
     * Returns the features for this project.
     *
     * @param projectName the project name.
     * @return a list of features.
     */
    public List<IProjectFeature> getFeatures(final Project project) {

        if (this.features == null) {
            LOGGER.info("getFeatures : no features.");
            return Lists.newArrayList();
        }

        final ProjectDto projectDto = ProjectDtoFactory.convert(project);
        final List<IProjectFeature> result = Lists.newArrayList();;

        for (final IProjectFeatues projectFeature : this.features) {
            final List<IProjectFeature> features = projectFeature.getFeatures(projectDto);
            if (features != null) {
                result.addAll(features);
            }
        }
        LOGGER.info("getVersions : the project \"{}\" has {} features.", project.getName(), result.size());
        return result;
    }

    public List<IProjectFeature> getFeaturesOfCurrentUser(Project project) {

        if (this.features == null) {
            LOGGER.info("getFeaturesOfCurrentUser : no features.");
            return Lists.newArrayList();
        }
        
        final String login = this.currentUserService.getCurrentUserLogin();
        final ProjectDto projectDto = ProjectDtoFactory.convert(project);

        final List<IProjectFeature> result = Lists.newArrayList();

        for (final IProjectFeatues projectFeature : this.features) {
            final List<IProjectFeature> features = projectFeature.getFeatures(projectDto);
            if (features != null) {
                for (final IProjectFeature feature : features) {
                    final String assignee = feature.getAssignee();
                    if (assignee.isEmpty() || assignee.equalsIgnoreCase(login)) {
                        result.add(feature);
                    }
                }
            }
        }
        LOGGER.info("getFeaturesOfCurrentUser : the project \"{}\" has {} features.", project.getName(), result.size());
        return result;
    }

}
