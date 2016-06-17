package fr.echoes.labs.ksf.cc.plugins.foreman;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanDAOExtension;
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectDashboardExtension;
import fr.echoes.labs.pluginfwk.api.extension.IExtension;

public class ForemanExtensionGuiceModule extends AbstractModule{

	@Override
	protected void configure() {

		final Multibinder<IExtension> newSetBinder = Multibinder.newSetBinder(binder(), IExtension.class);
		newSetBinder.addBinding().to(ForemanProjectDashboardExtension.class);
		newSetBinder.addBinding().to(ForemanDAOExtension.class);
	}
	
}
