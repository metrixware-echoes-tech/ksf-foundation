package fr.echoes.lab.ksf.cc.plugins.foreman;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import fr.echoes.lab.ksf.cc.plugins.foreman.extensions.ForemanDAOExtension;
import fr.echoes.lab.ksf.cc.plugins.foreman.extensions.ForemanProjectDashboardExtension;
import fr.echoes.lab.ksf.extensions.api.IExtension;

public class ForemanExtensionGuiceModule extends AbstractModule{

	@Override
	protected void configure() {

		final Multibinder<IExtension> newSetBinder = Multibinder.newSetBinder(binder(), IExtension.class);
		newSetBinder.addBinding().to(ForemanProjectDashboardExtension.class);
		newSetBinder.addBinding().to(ForemanDAOExtension.class);
	}
	
}
