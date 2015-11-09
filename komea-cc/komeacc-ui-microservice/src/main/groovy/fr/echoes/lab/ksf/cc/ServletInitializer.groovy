package fr.echoes.lab.ksf.cc

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer

public class ServletInitializer extends SpringBootServletInitializer {

	protected SpringApplicationBuilder configure(
			final SpringApplicationBuilder application) {
		return application.sources(KomeaCCGuiApplication.class)
	}
}
