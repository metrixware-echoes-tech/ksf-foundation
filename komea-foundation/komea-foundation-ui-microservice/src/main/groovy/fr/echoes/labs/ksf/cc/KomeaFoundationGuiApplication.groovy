package fr.echoes.labs.ksf.cc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@ComponentScan(value=["com.tocea", "fr.echoes.labs"])
@EnableAutoConfiguration
@EnableConfigurationProperties
//@EnableTransactionManagement
@EnableMongoRepositories(value=["com.tocea", "fr.echoes.labs"])
@EnableMongoAuditing
//@EnableWebMvc
public class KomeaCCGuiApplication {

	public static void main(final String[] args) {
		SpringApplication.run(KomeaCCGuiApplication.class, args)
	}
}
