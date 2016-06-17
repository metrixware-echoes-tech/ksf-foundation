package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import com.tocea.corolla.utils.domain.KsfDomainException;

/**
 * The Class ProjectDashboardWidgetNotFoundException.
 */
public class ProjectDashboardWidgetNotFoundException extends KsfDomainException {

	public ProjectDashboardWidgetNotFoundException(final String widgetID) {
		super("The Project dashboard widget " + widgetID + " does not exist.");
	}

}
