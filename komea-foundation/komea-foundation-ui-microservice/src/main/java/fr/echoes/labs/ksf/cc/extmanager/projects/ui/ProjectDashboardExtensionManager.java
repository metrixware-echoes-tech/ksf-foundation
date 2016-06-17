package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectDashboardExtension;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

/**
 * This service manages the plugins that alter the poject dashboard page;
 *
 * @author sleroy
 *
 */
@Service("projectDashboardWidgets")
public class ProjectDashboardExtensionManager implements IProjectDashboardExtensionManager {

	private final class FindProjectDashboardWidgetByIdPredicate implements Predicate<ProjectDashboardWidget> {

		private final String widgetID;

		public FindProjectDashboardWidgetByIdPredicate(final String widgetID) {
			this.widgetID = widgetID;
		}

		@Override
		public boolean apply(final ProjectDashboardWidget projectDashboardWidget) {
			return this.widgetID.equals(projectDashboardWidget.getWidgetId());
		}
	}

	private static final Logger		LOGGER	= LoggerFactory.getLogger(ProjectDashboardExtensionManager.class);

	private final ExtensionManager	extensionManager;

	@Autowired
	public ProjectDashboardExtensionManager(final ExtensionManager extensionManager) {
		super();
		this.extensionManager = extensionManager;
	}

	@Override
	public List<IProjectTabPanel> getDashboardPanels(final String projectKey) {
		final List<ProjectDashboardWidget> widgets = this.getDashboardWidgets();
		final List<IProjectTabPanel> panels = Lists.newArrayList();

		for (final ProjectDashboardWidget widget : widgets) {
			panels.addAll(widget.getTabPanels(projectKey));
		}

		return panels;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extmanager.projects.ui.IProjectDashboardExtensionManager#getDashboardWidget(java.lang.String)
	 */
	@Override
	public ProjectDashboardWidget getDashboardWidget(final String widgetID) throws ProjectDashboardWidgetNotFoundException {
		Validate.notEmpty(widgetID);
		final List<ProjectDashboardWidget> dashboardWidgets = this.getDashboardWidgets();
		final ProjectDashboardWidget projectDashboardWidget = Iterators.find(dashboardWidgets.iterator(),
				new FindProjectDashboardWidgetByIdPredicate(widgetID));
		if (projectDashboardWidget != null) {
			return projectDashboardWidget;
		} else {
			throw new ProjectDashboardWidgetNotFoundException(widgetID);
		}
	}

	@Override
	public List<ProjectDashboardWidget> getDashboardWidgets() {

		final List<IProjectDashboardExtension> extensions = this.extensionManager.findExtensions(IProjectDashboardExtension.class);

		if (extensions == null || extensions.isEmpty()) {
			LOGGER.info("GetDashboardWidgets : no extensions");
			return Lists.newArrayList();
		}

		LOGGER.debug("GetDashboardWidgets : extensions loaded={}", extensions.size());
		final ProjectDashboardWidgets projectDashboardWidgets = new ProjectDashboardWidgets();
		for (final IProjectDashboardExtension extension : extensions) {
			LOGGER.debug("GetDashboardWidgets : extension {}", extension);
			extension.reclaimProjectDashboardWidget(projectDashboardWidgets);
		}
		return projectDashboardWidgets.getWidgets();
	}

}
