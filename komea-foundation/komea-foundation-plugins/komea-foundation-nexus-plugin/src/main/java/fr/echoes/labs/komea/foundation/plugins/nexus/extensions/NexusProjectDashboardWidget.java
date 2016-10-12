package fr.echoes.labs.komea.foundation.plugins.nexus.extensions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.MenuAction;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.plugins.nexus.services.NexusConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.nexus.services.NexusNameResolver;
import fr.echoes.labs.ksf.cc.plugins.nexus.utils.NexusConstants;
import fr.echoes.labs.ksf.plugins.utils.ThymeleafTemplateEngineUtils;

/**
 * @author dcollard
 *
 */
@Component
public class NexusProjectDashboardWidget implements ProjectDashboardWidget {

    private static TemplateEngine templateEngine = ThymeleafTemplateEngineUtils.createTemplateEngine();

    private static final Logger LOGGER = LoggerFactory.getLogger(NexusProjectDashboardWidget.class);

    @Autowired
    private NexusConfigurationService configurationService;
    
    @Autowired
    private NexusNameResolver nameResolver;

    @Autowired
    private MessageSource messageResource;

    @Autowired
    private IProjectDAO projectDao;

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
        return new MessageSourceAccessor(NexusProjectDashboardWidget.this.messageResource).getMessage("foundation.nexus");
    }

    @Override
    public List<IProjectTabPanel> getTabPanels(final String projectKey) {
        
    	final Project project = this.projectDao.findByKey(projectKey);

        final IProjectTabPanel iframePanel = new IProjectTabPanel() {

            @Override
            public String getTitle() {
                return new MessageSourceAccessor(messageResource).getMessage("foundation.nexus.tab.title");
            }

            @Override
            public String getContent() {

                final Context ctx = new Context();

                final String url = new StringBuilder(configurationService.getUrl())
                	.append("/#view-repositories;")
                	.append(nameResolver.getRepositoryKey(project))
                	.append("~browsestorage")
                	.toString();
                
                LOGGER.info("[nexus] project URL : {}", url);

                ctx.setVariable("nexusURL", url);

                return templateEngine.process("nexusManagementPanel", ctx);
            }

            @Override
            public String getIconUrl() {
                return NexusProjectDashboardWidget.this.getIconUrl();
            }

            @Override
            public String getId() {
                return NexusConstants.ID;
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
        return NexusConstants.ID;
    }

}
