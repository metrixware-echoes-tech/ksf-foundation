package com.tocea.corolla

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
//@EnableJpaAuditing
//@EnableJpaRepositories
@EnableTransactionManagement
//@EnableWebMvc
class CorollaGuiApplication {

	public static void main(final String[] args) {
		SpringApplication.run(CorollaGuiApplication.class, args)
	}
}
