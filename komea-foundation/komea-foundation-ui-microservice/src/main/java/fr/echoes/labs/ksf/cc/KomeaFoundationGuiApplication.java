package fr.echoes.labs.ksf.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import fr.echoes.labs.ksf.cc.KomeaFoundationGuiApplication;
import fr.echoes.labs.ksf.cc.configuration.FoundationConsts;

@SpringBootApplication
@ComponentScan(value={"com.tocea", "fr.echoes.labs"})
@EnableAutoConfiguration
@EnableConfigurationProperties
//@EnableTransactionManagement
@EnableMongoRepositories(value={"com.tocea", "fr.echoes.labs"})
@EnableMongoAuditing
//@EnableWebMvc
@PropertySources({
    @PropertySource(value = "classpath:" + FoundationConsts.CONF_FILENAME),
    @PropertySource(value = "file:config/" + FoundationConsts.CONF_FILENAME, ignoreResourceNotFound = true),
    @PropertySource(value = "file:conf/" + FoundationConsts.CONF_FILENAME, ignoreResourceNotFound = true),
    @PropertySource(value = "file:" + FoundationConsts.CONF_FILENAME, ignoreResourceNotFound = true)
})
public class KomeaFoundationGuiApplication {
	
	public static void main(final String[] args) {
		SpringApplication.run(KomeaFoundationGuiApplication.class, args);
	}
}
