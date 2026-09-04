package com.c2.lc.ms.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class },
						scanBasePackages = {"com.c2.lc.lib", "com.c2.lc.ms.security"})
@EnableJpaRepositories
public class MsSecurityApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(MsSecurityApplication.class)
				.properties("spring.config.name:application,db,log,spy,messages", "spring.config.location:classpath:/")
				.build()
				.run(args);
	}
}