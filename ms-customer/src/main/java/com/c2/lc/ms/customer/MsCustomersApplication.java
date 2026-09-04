package com.c2.lc.ms.customer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@EnableScheduling
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }, scanBasePackages = {"com.c2.lc.lib", "com.c2.lc.ms.customer"})
@ComponentScan(basePackages = {"com.c2.lc.lib", "com.c2.lc.ms.customer"})
public class MsCustomersApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(MsCustomersApplication.class)
				.properties("spring.config.name:application,db,log,spy", "spring.config.location:classpath:/")
				.build()
				.run(args);
	}
}
